package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ObjectMapperFactory;
import com.dropchop.shiro.cdi.DefaultShiroEnvironmentProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@Alternative
@Priority(1)
@ApplicationScoped
public class TestShiroEnvironmentProvider extends DefaultShiroEnvironmentProvider {

  @Inject
  ObjectMapperFactory objectMapperFactory;

  private void configureMappers() {
  }

  /**
   * ObjectMapper customization/extension point. Add custom polymorphic mappings here.
   * This is needed because of polymorphism if you want to use existing To[XY]TagMapper.
   * If you provide your own To[XY]MyCustomTagMapper, then you (probably) don't need this.
   */
  @Produces
  @ApplicationScoped
  ObjectMapper getOutputObjectMapper() {
    configureMappers();
    return objectMapperFactory.createFilteringObjectMapper();
  }

  @Produces
  @ApplicationScoped
  @RecycloneType(RECYCLONE_DEFAULT)
  ObjectMapper getInternalObjectMapper() {
    configureMappers();
    return objectMapperFactory.createNonFilteringObjectMapper();
  }
}
