package com.dropchop.recyclone.base.api.model.filtering;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 24.
 */
@SuppressWarnings("unused")
public interface JsonSerializationTypeConfig {
  JsonSerializationTypeConfig addSubType(String propertyValue, Class<?> subType);

  JsonSerializationTypeConfig addMixIn(Class<?> type, Class<?> mixIn);

  Map<String, Class<?>> getSubTypeMap();

  Map<Class<?>, Class<?>> getMixIns();
}
