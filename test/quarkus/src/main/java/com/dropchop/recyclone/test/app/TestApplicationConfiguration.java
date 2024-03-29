package com.dropchop.recyclone.test.app;

import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.ELanguageGroup;
import com.dropchop.recyclone.rest.jaxrs.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class TestApplicationConfiguration {

  @Inject
  ObjectMapperFactory objectMapperFactory;

  @Inject
  MapperSubTypeConfig mapperConfig;

  /**
   * ObjectMapper customization/extension point. Add own polymorphic mappings here.
   * This is needed because of polymorphism if you want to use existing To[XY]TagMapper.
   * If provide your own To[XY]MyCustomTagMapper then you (probably) don't need this.
   */
  @Produces
  @ApplicationScoped
  ObjectMapper getObjectMapper() {
    //mapping is already there collected from the @SubclassMapping annotation of the TagToDtoMapper,
    //but we add it again here just for demonstration
    mapperConfig.addBidiMapping(LanguageGroup.class, ELanguageGroup.class);
    return objectMapperFactory.createObjectMapper();
  }
}
