package com.dropchop.recyclone.quarkus.runtime.registry;

import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@DefaultBean
@ApplicationScoped
public class MapperSubTypeConfigImpl implements MapperSubTypeConfig {
  private final Map<Class<?>, Class<?>> forwardMapping = new ConcurrentHashMap<>();
  private final Map<Class<?>, Class<?>> reverseMapping = new ConcurrentHashMap<>();

  @Override
  public MapperSubTypeConfig addBidiMapping(Class<?> fromClassName, Class<?> toClassName) {
    forwardMapping.put(toClassName, fromClassName);
    reverseMapping.put(fromClassName, toClassName);
    return this;
  }

  @Override
  public MapperSubTypeConfig addMapping(Class<?> fromClassName, Class<?> toClassName) {
    reverseMapping.put(toClassName, fromClassName);
    return this;
  }

  @Override
  public Class<?> mapsTo(Class<?> source) {
    Class<?> tmp = forwardMapping.get(source);
    if (tmp != null) {
      return tmp;
    }
    return reverseMapping.get(source);
  }
}
