package com.dropchop.recyclone.quarkus.runtime.app;

import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import io.quarkus.arc.DefaultBean;
import io.vertx.core.impl.ConcurrentHashSet;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@DefaultBean
@ApplicationScoped
public class MapperSubTypeConfigImpl implements MapperSubTypeConfig {
  private final Map<Class<?>, Collection<Class<?>>> targetLookup = new ConcurrentHashMap<>();
  private final Map<Class<?>, Collection<Class<?>>> sourceLookup = new ConcurrentHashMap<>();
  private final Set<Class<?>> entityRootMarkers = new ConcurrentHashSet<>();

  @Override
  public MapperSubTypeConfig addBidiMapping(Class<?> fromClassName, Class<?> toClassName) {
    targetLookup.computeIfAbsent(toClassName, aClass -> new LinkedHashSet<>()).add(fromClassName);
    sourceLookup.computeIfAbsent(fromClassName, aClass -> new LinkedHashSet<>()).add(toClassName);
    return this;
  }

  @Override
  public MapperSubTypeConfig addMapping(Class<?> fromClassName, Class<?> toClassName) {
    sourceLookup.computeIfAbsent(fromClassName, aClass -> new LinkedHashSet<>()).add(toClassName);
    return this;
  }

  @Override
  public Collection<Class<?>> mapsTo(Class<?> source) {
    LinkedHashSet<Class<?>> result = new LinkedHashSet<>();
    Collection<Class<?>> tmp = targetLookup.get(source);
    if (tmp != null) {
      result.addAll(tmp);
    }
    tmp = sourceLookup.get(source);
    if (tmp != null) {
      result.addAll(tmp);
    }
    return result;
  }

  public MapperSubTypeConfig addEntityRootMarker(Class<?> entityRootMarker) {
    entityRootMarkers.add(entityRootMarker);
    return this;
  }

  public Class<?> getEntityRootMarkerFor(Class<?> entityClass) {
    for (Class<?> entityRootMarker : entityRootMarkers) {
      if (entityRootMarker.isAssignableFrom(entityClass)) {
        return entityRootMarker;
      }
    }
    return null;
  }
}
