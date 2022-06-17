package com.dropchop.recyclone.service.api.mapping;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 06. 22.
 */
public interface ContextAwarePolymorphicRegistry {
  Class<?> mapsTo(Class<?> source, String context);
}
