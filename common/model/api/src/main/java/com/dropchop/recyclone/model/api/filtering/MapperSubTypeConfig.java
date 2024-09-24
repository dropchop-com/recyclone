package com.dropchop.recyclone.model.api.filtering;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 24.
 */
public interface MapperSubTypeConfig {
  MapperSubTypeConfig addBidiMapping(Class<?> fromClassName, Class<?> toClassName);

  MapperSubTypeConfig addMapping(Class<?> fromClassName, Class<?> toClassName);

  Class<?> mapsTo(Class<?> source);
}
