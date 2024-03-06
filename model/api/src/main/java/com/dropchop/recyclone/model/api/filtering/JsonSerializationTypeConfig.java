package com.dropchop.recyclone.model.api.filtering;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
public interface JsonSerializationTypeConfig {
  JsonSerializationTypeConfig addSubType(String propertyValue, Class<?> subType);

  JsonSerializationTypeConfig addMixIn(Class<?> type, Class<?> mixIn);

  Map<String, Class<?>> getSubTypeMap();

  Map<Class<?>, Class<?>> getMixIns();
}
