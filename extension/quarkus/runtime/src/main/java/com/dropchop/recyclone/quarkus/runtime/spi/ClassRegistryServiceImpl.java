package com.dropchop.recyclone.quarkus.runtime.spi;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.service.api.ClassRegistryService;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
@DefaultBean
@ApplicationScoped
public class ClassRegistryServiceImpl implements ClassRegistryService {

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
