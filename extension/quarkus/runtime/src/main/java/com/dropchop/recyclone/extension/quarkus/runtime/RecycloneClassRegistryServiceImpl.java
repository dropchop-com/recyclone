package com.dropchop.recyclone.extension.quarkus.runtime;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.api.filtering.RecycloneClassRegistryService;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
@ApplicationScoped
public class RecycloneClassRegistryServiceImpl implements RecycloneClassRegistryService {

  private final JsonSerializationTypeConfig jsonSerializationTypeConfig = new JsonSerializationTypeConfigImpl();
  private final MapperSubTypeConfig mapperTypeConfig = new MapperSubTypeConfigImpl();

  @Override
  @Produces
  @DefaultBean
  @ApplicationScoped
  public JsonSerializationTypeConfig getJsonSerializationTypeConfig() {
    return jsonSerializationTypeConfig;
  }

  @Override
  @Produces
  @DefaultBean
  @ApplicationScoped
  public MapperSubTypeConfig getMapperTypeConfig() {
    return mapperTypeConfig;
  }
}
