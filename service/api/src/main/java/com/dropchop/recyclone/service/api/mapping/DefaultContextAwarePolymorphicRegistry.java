package com.dropchop.recyclone.service.api.mapping;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
@Slf4j
public class DefaultContextAwarePolymorphicRegistry implements ContextAwarePolymorphicRegistry {

  private static final String PROP_PREFIX = "dropchop.scan-polymorphic";
  private final Map<String, Map<Class<?>, Class<?>>> registry = new ConcurrentHashMap<>();

  public DefaultContextAwarePolymorphicRegistry() {

  }

  @Override
  public Class<?> mapsTo(Class<?> source, String context) {
    Map<Class<?>, Class<?>> contextRegistry = registry.get(context);
    if (contextRegistry == null) {
      return null;
    }
    return contextRegistry.get(source);
  }
}
