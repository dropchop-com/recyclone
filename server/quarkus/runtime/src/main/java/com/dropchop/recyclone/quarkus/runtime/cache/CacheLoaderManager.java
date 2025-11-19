package com.dropchop.recyclone.quarkus.runtime.cache;

import com.dropchop.recyclone.base.api.service.caching.CacheLoader;
import com.dropchop.recyclone.base.api.service.caching.CacheLoader.Listener;
import com.dropchop.recyclone.base.api.service.caching.CacheLoader.LoadListener;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.scheduler.Scheduler;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.Readiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static com.dropchop.recyclone.base.api.model.invoke.Constants.Messages.CACHE_STORAGE_INIT;

/**
 * Manages cache loaders and their lifecycle, providing mechanisms for scheduling and refreshing
 * their loading processes. This class coordinates loading execution, triggers appropriate lifecycle
 * events, and schedules periodic loader refreshes.
 * <br />
 * CacheLoaderManager uses dependency injection to manage instances of CacheLoaders and their
 * respective listeners, ensuring consistent and efficient execution of caching logic.
 * <br />
 * CacheLoaderManager is a Quarkus CDI bean and is automatically initialized during application startup.
 * This is when cache loaders are registered, run to populate the cache,
 * and scheduled to run again at regular intervals.
 * <br />
 * The code is rather complicated due to the need to support multiple types of cache loaders and listeners,
 * ensuring each loader loads and iterates the data from the datasource only once per interval.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 4. 25.
 */
@ApplicationScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class CacheLoaderManager {

  private final Logger log = LoggerFactory.getLogger(CacheLoaderManager.class);

  private ExecutorService startupLoadExecutor;

  @Inject
  Instance<CacheLoader<?>> loaders;

  @Inject
  Instance<CacheLoader.Listener<?, ?>> consumers;

  /**
   * Refreshes and reloads the given cache loader while managing its lifecycle events.
   * <br />
   * This method initializes and executes the loading process for the specified cache loader.
   * It triggers the lifecycle events of associated listeners, including start, item-loaded,
   * and end events. Listeners are obtained from registered LifecycleListenerProviders.
   * <br />
   * The loading process involves notifying each listener at the start of loading, reacting
   * to each loaded item, and finalizing the process with an end lifecycle event.
   *
   * @param <S>          the source type of items managed and loaded by the cache loader
   * @param <C>          the type of the loading context associated with the loading process
   * @param cacheLoader  the cache loader whose loading process is to be refreshed
   */
  private <S, C extends CacheLoader.LoadingContext> void refreshLoader(CacheLoader<S> cacheLoader) {
    Map<Listener<S, C>, C> contexts = new HashMap<>();
    for (CacheLoader.Listener<?, ?> listener : consumers) {
      if (listener == null) {
        continue;
      }
      @SuppressWarnings("unchecked")
      C context = (C) listener.onStart(cacheLoader);
      if (context != null) {
        //noinspection unchecked
        contexts.put((Listener<S, C>) listener, context);
      }
    }

    if (!contexts.isEmpty()) {
      // Invoke a cache loader load with wrapped callback so we can delegate to all interested listeners
      cacheLoader.load((LoadListener<S, C>) (__, item) -> {
        for (Map.Entry<Listener<S, C>, C> entry : contexts.entrySet()) {
          LoadListener<S, C> listener = entry.getKey();
          listener.onItem(entry.getValue(), item);
        }
      });

      for (Map.Entry<Listener<S, C>, C> entry : contexts.entrySet()) {
        Listener<S, C> listener = entry.getKey();
        listener.onEnd(entry.getValue());
      }
    }
  }

  @Inject
  Scheduler scheduler;

  @Inject
  @Readiness
  CacheReadiness readiness;

  @SuppressWarnings("unused")
  @ConsumeEvent(CACHE_STORAGE_INIT)
  public void onInitSignal(String message) {
    log.info("Cache loading initialization start with message [{}={}].", CACHE_STORAGE_INIT, message);

    startupLoadExecutor = Executors.newSingleThreadExecutor();

    // We must load caches for the first time off of the Vert.x thread, which has blocking and running time-constraints.
    // We must start load after connections were established to the database(s)
    CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(
        () -> loaders.forEach(loader -> {
          if (loader == null) {
            return;
          }
          if (!loader.isEnabled()) {
            return;
          }

          try {
            refreshLoader(loader);
            int interval = loader.getReloadIntervalSeconds();
            int delaySeconds = ThreadLocalRandom.current().nextInt(0, interval / 2);
            log.info(
              "Scheduling cache loader [{}] with interval [{}] seconds and delay [{}] seconds.",
              loader.getClass().getName(), interval, delaySeconds
            );

            String jobId = "cache_loader." + loader.getClass().getName();
            scheduler.newJob(jobId)
              .setDelayed(delaySeconds + "s")
              .setInterval(interval + "s")
              .setTask(executionContext -> refreshLoader(loader))
              .schedule();
          } catch (Exception e) {
            log.error("Failed to initialize cache loader: {}!", loader.getClass().getName(), e);
          }
        }),
        startupLoadExecutor
    ).thenRun(
        () -> readiness.markReady()
    );
  }

  public void onStop(@Observes ShutdownEvent event) {
    if (startupLoadExecutor != null && !startupLoadExecutor.isShutdown()) {
      log.info("Shutting down cache loading executor.");
      startupLoadExecutor.shutdown();

      try {
        if (!startupLoadExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
          log.warn("Cache loading executor did not terminate gracefully, forcing shutdown!");
          startupLoadExecutor.shutdownNow();
        }
      } catch (InterruptedException e) {
        log.warn("Interrupted while waiting for cache loading executor to terminate!");
        startupLoadExecutor.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }
    scheduler.getScheduledJobs().forEach(
        t -> {
          scheduler.unscheduleJob(t.getId());
          log.info("Unscheduled cache loader job [{}].", t.getId());
        }
    );
  }
}
