package com.dropchop.recyclone.base.api.model.filtering;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 24.
 */
@SuppressWarnings("unused")
public interface MapperSubTypeConfig {
  MapperSubTypeConfig addBidiMapping(Class<?> fromClassName, Class<?> toClassName);

  MapperSubTypeConfig addMapping(Class<?> fromClassName, Class<?> toClassName);

  Collection<Class<?>> mapsTo(Class<?> source);

  MapperSubTypeConfig addEntityRootMarker(Class<?> entityRootMarker);

  Class<?> getEntityRootMarkerFor(Class<?> entityClass);
}
