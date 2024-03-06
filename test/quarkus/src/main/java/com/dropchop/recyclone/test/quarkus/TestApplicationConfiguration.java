package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.api.filtering.RecycloneClassRegistryService;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.dto.tagging.CountryGroup;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.model.dto.tagging.Owner;
import com.dropchop.recyclone.model.entity.jpa.tagging.ECountryGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.ELanguageGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.EOwner;
import com.dropchop.recyclone.rest.jaxrs.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

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

  @Produces
  @ApplicationScoped
  ObjectMapper getObjectMapper() {
    mapperConfig
        .addBidiMapping(Owner.class, EOwner.class)
        .addBidiMapping(LanguageGroup.class, ELanguageGroup.class)
        .addBidiMapping(CountryGroup.class, ECountryGroup.class);
    return objectMapperFactory.createObjectMapper();
  }
}
