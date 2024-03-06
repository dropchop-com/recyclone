package com.dropchop.recyclone.extension.quarkus.runtime;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@SuppressWarnings({"unused", "LombokGetterMayBeUsed"})
public class JsonSerializationTypeConfigImpl implements JsonSerializationTypeConfig {
  final Map<Class<?>, Class<?>> mixIns = new ConcurrentHashMap<>();
  final Map<String, Class<?>> subTypeMap = new ConcurrentHashMap<>();

  @Override
  public JsonSerializationTypeConfig addSubType(String propertyValue, Class<?> subType) {
    subTypeMap.put(propertyValue, subType);
    return this;
  }

  @Override
  public JsonSerializationTypeConfig addMixIn(Class<?> type, Class<?> mixIn) {
    mixIns.put(type, mixIn);
    return this;
  }

  @Override
  public Map<String, Class<?>> getSubTypeMap() {
    return subTypeMap;
  }

  @Override
  public Map<Class<?>, Class<?>> getMixIns() {
    return mixIns;
  }
}
