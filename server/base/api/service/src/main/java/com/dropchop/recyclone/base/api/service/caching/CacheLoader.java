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
   * Represents a context for managing the loading process within a cache loader.
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
  interface LoadingListener<I, C extends LoadingContext> {
    void onItem(C context, I item);
  }

  /**
   * Represents a listener interface that extends {@link LoadingListener} to handle adaptive
   * item processing during a loading operation within a specific {@link LoadingContext}.
   * This interface is designed to support item adaptation and provides a mechanism for
   * reacting when both the original and the adapted forms of an item become available.
   *
   * @param <I> the type of the original items being loaded
   * @param <A> the type of the adapted items resulting from the transformation of the original items
   * @param <C> the type of the loading context associated with the loading process, extending {@link LoadingContext}
   */
  interface AdaptiveLoadingListener<I, A, C extends LoadingContext> extends LoadingListener<I, C> {
    void onItem(C context, I item, A adapted);

    // never called, so we implement it here to avoid boiler plating the implementation code.
    default void onItem(C context, I item) {
    }
  }

  /**
   * Represents a specialized lifecycle listener that manages the beginning and end of
   * a loading process for a given cache loader. This interface extends the
   * {@link LoadingListener} interface, enabling it to react to individual item loading events
   * as well as the broader lifecycle phases of the loading process.
   *
   * @param <I> the type of items being loaded by the cache loader
   * @param <C> the type of the loading context associated with the loading process
   */
  interface Listener<I, C extends LoadingContext> extends LoadingListener<I, C> {

    /**
     * Handles the start of the loading process for the given cache loader.
     * <br>
     * This method is responsible for initiating the lifecycle event of starting
     * the loading process. It allows the implementer to prepare or configure a
     * specific loading context before the loading begins.
     *
     * @param cacheLoader the cache loader associated with the loading process
     * @return the context associated with the loading process or null if not interested!
     */
    C onStart(CacheLoader<?> cacheLoader);
    void onEnd(C context);
  }

  /**
   * Represents a specialized lifecycle listener that extends the functionality
   * of both {@link AdaptiveLoadingListener} and {@link Listener}.
   * This interface provides a combined mechanism for handling lifecycle events
   * and adapting items during the loading process.
   * <br />
   * The {@code AdaptiveListener} facilitates seamless integration of
   * item adaptation with the broader loading lifecycle management, enabling
   * implementers to define transformation rules for loaded items and to
   * manage the start and end of the loading process within a given context.
   *
   * @param <I> the type of items being loaded
   * @param <A> the type of adapted items resulting from the transformation of loaded items
   * @param <C> the type of the loading context associated with the loading process,
   *            extending {@link LoadingContext}
   */
  @SuppressWarnings("unused")
  interface AdaptiveListener<I, A, C extends LoadingContext> extends
      AdaptiveLoadingListener<I, A, C>,
      Listener<I, C> {
  }

  String getName();

  Integer getReloadIntervalSeconds();

  boolean isEnabled();

  <A> Adapter<I, A> getAdapter();

  <C extends LoadingContext> void load(LoadingListener<I, C> listener);
}
