package com.dropchop.recyclone.extension.quarkus.runtime;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.api.filtering.RecycloneClassRegistryService;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
@DefaultBean
@ApplicationScoped
public class RecycloneClassRegistryServiceImpl implements RecycloneClassRegistryService {

  @Inject
  JsonSerializationTypeConfig jsonSerializationTypeConfig;

  @Inject
  MapperSubTypeConfig mapperTypeConfig;

  @Override
  public JsonSerializationTypeConfig getJsonSerializationTypeConfig() {
    return jsonSerializationTypeConfig;
  }

  @Override
  public MapperSubTypeConfig getMapperTypeConfig() {
    return mapperTypeConfig;
  }
}
