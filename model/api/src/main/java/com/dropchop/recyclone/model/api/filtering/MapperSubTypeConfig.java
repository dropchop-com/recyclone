package com.dropchop.recyclone.model.api.filtering;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperTypeConfig {
  private final Map<Class<?>, Class<?>> forwardMapping = new ConcurrentHashMap<>();
  private final Map<Class<?>, Class<?>> reverseMapping = new ConcurrentHashMap<>();

  public MapperTypeConfig addBidiMapping(Class<?> fromClassName, Class<?> toClassName) {
    forwardMapping.put(toClassName, fromClassName);
    reverseMapping.put(fromClassName, toClassName);
    return this;
  }

  public MapperTypeConfig addMapping(Class<?> fromClassName, Class<?> toClassName) {
    reverseMapping.put(toClassName, fromClassName);
    return this;
  }

  public Class<?> mapsTo(Class<?> source) {
    Class<?> tmp = forwardMapping.get(source);
    if (tmp != null) {
      return tmp;
    }
    return reverseMapping.get(source);
  }
}
