package com.dropchop.recyclone.model.api.filtering;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@SuppressWarnings("unused")
public interface PolymorphicRegistry {

  class SerializationConfig {

    final Map<Class<?>, Class<?>> mixIns = new LinkedHashMap<>();
    final Map<String, Class<?>> subTypeMap = new LinkedHashMap<>();

    public SerializationConfig addSubType(String propertyValue, Class<?> subType) {
      subTypeMap.put(propertyValue, subType);
      return this;
    }

    public SerializationConfig addMixIn(Class<?> type, Class<?> mixIn) {
      mixIns.put(type, mixIn);
      return this;
    }

    public Map<String, Class<?>> getSubTypeMap() {
      return subTypeMap;
    }

    public Map<Class<?>, Class<?>> getMixIns() {
      return mixIns;
    }
  }

  Class<?> mapsTo(Class<?> source);

  Iterable<SerializationConfig> getSerializationConfigs();
}
