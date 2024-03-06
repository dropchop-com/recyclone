package com.dropchop.recyclone.model.api.filtering;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
public interface RecycloneClassRegistryService {
  JsonSerializationTypeConfig getJsonSerializationTypeConfig();

  MapperSubTypeConfig getMapperTypeConfig();
}
