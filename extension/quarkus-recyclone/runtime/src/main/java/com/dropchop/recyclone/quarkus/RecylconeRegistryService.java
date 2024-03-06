package com.dropchop.recyclone.quarkus;

import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistryConfig;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 02. 24.
 */
@Singleton
@SuppressWarnings("unused")
public class RecylconeRegistryService implements PolymorphicRegistry, PolymorphicRegistryConfig {

  //build-time collected string-type structures
  private final Map<String, String> mappingClassMap = new ConcurrentHashMap<>();
  private final Map<String, String> serializationTypeMap = new ConcurrentHashMap<>();

  //run-time collected class-type structures
  private final Collection<SerializationConfig> serializationConfigs = ConcurrentHashMap.newKeySet();
  private final Map<Class<?>, Class<?>> dtoEntityMapping = new ConcurrentHashMap<>();

  public RecylconeRegistryService() {
    // Constructor for MyService.
  }

  void addSerializationTypeMap(String type, String className) {
    serializationTypeMap.put(type, className);
  }

  void addMappingClassMap(String fromClassName, String toClassName) {
    mappingClassMap.put(toClassName, fromClassName);
    mappingClassMap.put(fromClassName, toClassName);
  }

  @Override
  public PolymorphicRegistryConfig registerDtoEntityMapping(Class<?> source, Class<?> target) {
    dtoEntityMapping.put(source, target);
    dtoEntityMapping.put(target, source);
    return this;
  }

  @Override
  public PolymorphicRegistryConfig registerSerializationConfig(SerializationConfig serializationConfig) {
    this.serializationConfigs.add(serializationConfig);
    return this;
  }

  public Class<?> mapsTo(Class<?> source) {
    Class<?> target = dtoEntityMapping.get(source);
    if (target == null) {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      for (Map.Entry<String, String> entry : mappingClassMap.entrySet()) {
        try {
          dtoEntityMapping.put(cl.loadClass(entry.getKey()), cl.loadClass(entry.getValue()));
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return dtoEntityMapping.get(source);
  }

  public Collection<SerializationConfig> getSerializationConfigs() {
    Set<SerializationConfig> configs = new LinkedHashSet<>();
    SerializationConfig defaultConfig = new SerializationConfig();
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    for (Map.Entry<String, String> entry : serializationTypeMap.entrySet()) {
      try {
        defaultConfig.addSubType(entry.getKey(), cl.loadClass(entry.getValue()));
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    configs.add(defaultConfig);
    configs.addAll(serializationConfigs);
    return configs;
  }
}
