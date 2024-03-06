package com.dropchop.recyclone.quarkus;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfigImpl;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfigImpl;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
@Singleton
public class RecycloneClassRegistryService {

  private final JsonSerializationTypeConfig jsonSerializationTypeConfig = new JsonSerializationTypeConfigImpl();
  private final MapperSubTypeConfig mapperTypeConfig = new MapperSubTypeConfigImpl();

  @Produces
  @DefaultBean
  public JsonSerializationTypeConfig getJsonSerializationTypeConfig() {
    return jsonSerializationTypeConfig;
  }

  @Produces
  @DefaultBean
  public MapperSubTypeConfig getMapperTypeConfig() {
    return mapperTypeConfig;
  }
}
