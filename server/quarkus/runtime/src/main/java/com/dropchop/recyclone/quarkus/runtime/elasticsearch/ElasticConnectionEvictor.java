package com.dropchop.recyclone.quarkus.runtime.elasticsearch;

import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 10. 2025.
 */
@ApplicationScoped
public class ElasticConnectionEvictor {

  private static final Logger log = LoggerFactory.getLogger(ElasticConnectionEvictor.class);

  private static final Duration EVICT_EVERY = Duration.ofMinutes(1);
  private static final Duration IDLE_THRESHOLD = Duration.ofMinutes(8);

  private final ScheduledExecutorService scheduler;
  private final Random jitter = new Random();
  private final AtomicReference<ScheduledFuture<?>> taskRef = new AtomicReference<>();

  public ElasticConnectionEvictor() {
    this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, "es-conn-evictor");
      t.setDaemon(true);
      return t;
    });
  }

  /**
   * Idempotent start. Safe if called multiple times or from multiple threads.
   * Pass the SAME connection manager you install into the HTTP client.
   */
  public void start(PoolingNHttpClientConnectionManager connManager) {
    if (connManager == null) {
      return;
    }

    // already started
    ScheduledFuture<?> current = taskRef.get();
    if (current != null && !current.isCancelled()) {
      return;
    }

    long initialDelaySec = jitter.nextInt(11); // 0–10s

    ScheduledFuture<?> newTask = scheduler.scheduleAtFixedRate(() -> {
      try {
        connManager.closeExpiredConnections();
        connManager.closeIdleConnections(IDLE_THRESHOLD.toMillis(), TimeUnit.MILLISECONDS);
      } catch (RejectedExecutionException ex) {
        // Usually means shutdown in progress
        log.info("Eviction skipped", ex);
      } catch (Throwable t) {
        log.warn("Unexpected error during ES connection eviction", t);
      }
    }, initialDelaySec, EVICT_EVERY.toSeconds(), TimeUnit.SECONDS);

    // CAS install; if someone beat us to it, cancel ours
    if (!taskRef.compareAndSet(current, newTask)) {
      newTask.cancel(false);
    } else if (current != null && !current.isCancelled()) {
      // We replaced a live task (rare race); cancel the old one
      current.cancel(false);
    }
  }

  public void onStop(@Observes ShutdownEvent event) {
    log.info("Shutting down Elasticsearch idle connection evictor...");
    ScheduledFuture<?> curr = taskRef.getAndSet(null);
    if (curr != null) {
      curr.cancel(false);
    }
    try {
      if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
        scheduler.shutdownNow();
      }
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      scheduler.shutdownNow();
    }
    log.info("Elasticsearch idle connection evictor stopped.");
  }
}
