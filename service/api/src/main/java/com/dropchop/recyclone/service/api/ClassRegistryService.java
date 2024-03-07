package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
public interface ClassRegistryService {
  JsonSerializationTypeConfig getJsonSerializationTypeConfig();

  MapperSubTypeConfig getMapperTypeConfig();
}
