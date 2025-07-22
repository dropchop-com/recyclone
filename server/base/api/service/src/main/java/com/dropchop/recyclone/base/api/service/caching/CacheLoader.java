package com.dropchop.recyclone.base.api.service.caching;


/**
 * Represents a generic contract for a cache loader, enabling the process of loading
 * items into a cache with specific configurations and lifecycle management.
 *
 * @param <I> the type of items managed and loaded by the cache loader
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 4. 25.
 */
public interface CacheLoader<I> {

  /**
   * Represents a context for managing the loading process state within a cache loader.
   * A LoadingContext defines the specific configuration and state required during
   * the lifecycle of the loading process.
   * <br />
   * Implementers of this interface are responsible for providing any data or functionality
   * needed to facilitate the loading operations, including initialization, handling intermediate
   * states, and cleanup after the loading operations have completed.
   * <br />
   * LoadingContext instances are typically associated with specific loading scenarios
   * and may encapsulate data or configurations specific to the loader.
   */
  interface LoadingContext {
  }

  /**
   * Represents a mechanism for adapting an item of one type into another type.
   * This interface allows the transformation of objects from the input type {@code I}
   * to the adapted type {@code A}, providing flexibility for integrating different
   * types of data or handling specific transformation logic.
   * <br />
   * Adapter implementations are typically used in contexts where data needs to be
   * processed or transformed for compatibility or further operations. For example,
   * within a loading or caching mechanism, items of one type may need to be converted
   * into another type to accommodate specific processing needs.
   *
   * @param <I> the type of the input item to be adapted
   * @param <A> the type of the adapted output after transformation
   */
  interface Adapter<I, A> {
    A adapt(I item);
  }

  /**
   * Represents a generic listener interface for responding to item loading events
   * within the context of a cache loader. This interface is designed to handle individual
   * item events during the loading process.
   *
   * @param <I> the type of items being loaded
   * @param <C> the type of the loading context associated with the loading process
   */
  interface LoadListener<I, C extends LoadingContext> {
    void onItem(C context, I item);
  }

  /**
   * Interface for listening to lifecycle events of a cache loading process.
   * Provides methods to handle the start and end of the loading lifecycle
   * with the ability to establish and manage a context specific to the loading session.
   *
   * @param <C> the type of the loading context, which must extend {@code LoadingContext}
   */
  interface LifecycleListener<C extends LoadingContext> {
    C onStart(CacheLoader<?> cacheLoader);
    void onEnd(C context);
  }

  /**
   * Represents a composite listener interface that combines the functionality of both the
   * {@link LoadListener} and {@link LifecycleListener} interfaces.
   *
   * <p>
   * This interface is designed to manage cache loading lifecycle events, allowing implementers
   * to listen and react to both item-specific loading events and overall loading lifecycle
   * events. It extends the functionality of {@link LoadListener} to handle per-item loading
   * operations and of {@link LifecycleListener} to manage the start and end of the loading
   * process.
   * </p>
   *
   * @param <I> the type of items being loaded
   * @param <C> the type of the loading context associated with the loading process, which
   *            must extend {@link LoadingContext}
   */
  interface Listener<I, C extends LoadingContext> extends LoadListener<I, C>, LifecycleListener<C> {
  }

  String getName();

  Integer getReloadIntervalSeconds();

  boolean isEnabled();

  <A> Adapter<I, A> getAdapter();

  <C extends LoadingContext> void load(LoadListener<I, C> listener);
}
