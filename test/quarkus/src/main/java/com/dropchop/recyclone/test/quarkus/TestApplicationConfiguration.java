package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry.SerializationConfig;
import com.dropchop.recyclone.model.dto.tagging.CountryGroup;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.model.dto.tagging.Owner;
import com.dropchop.recyclone.model.entity.jpa.tagging.ECountryGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.ELanguageGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.EOwner;
import com.dropchop.recyclone.quarkus.RecylconeRegistryService;
import com.dropchop.recyclone.rest.jaxrs.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class TestApplicationConfiguration {

  @Inject
  ObjectMapperFactory objectMapperFactory;

  @Produces
  ObjectMapper getObjectMapper() {
    return objectMapperFactory.createObjectMapper();
  }

  @Inject
  RecylconeRegistryService service;

  @Produces
  PolymorphicRegistry getPolymorphicRegistry() {
    service
        .registerDtoEntityMapping(Owner.class, EOwner.class)
        .registerDtoEntityMapping(LanguageGroup.class, ELanguageGroup.class)
        .registerDtoEntityMapping(CountryGroup.class, ECountryGroup.class);
    Collection<SerializationConfig> configs = service.getSerializationConfigs();
    return service;
  }
}
