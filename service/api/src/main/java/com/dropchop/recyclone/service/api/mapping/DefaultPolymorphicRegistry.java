package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistryConfig;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@Slf4j
@Alternative
@ApplicationScoped
public class DefaultPolymorphicRegistry implements PolymorphicRegistry, PolymorphicRegistryConfig {

  private final Map<Class<?>, Class<?>> dtoEntityRegistry = new LinkedHashMap<>();

  private final List<SerializationConfig> serializationConfigs = new ArrayList<>();

  @Override
  public Class<?> mapsTo(Class<?> source) {
    return dtoEntityRegistry.get(source);
  }

  @Override
  public PolymorphicRegistryConfig registerDtoEntityMapping(Class<?> source, Class<?> target) {
    dtoEntityRegistry.put(target, source);
    dtoEntityRegistry.put(source, target);
    return this;
  }

  public PolymorphicRegistryConfig registerSerializationConfig(SerializationConfig serializationConfig) {
    serializationConfigs.add(serializationConfig);
    return this;
  }

  @Override
  public Iterable<SerializationConfig> getSerializationConfigs() {
    return serializationConfigs;
  }
}
