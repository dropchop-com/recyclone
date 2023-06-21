package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.dto.security.LoginAccount;
import com.dropchop.recyclone.model.dto.security.TokenAccount;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.model.dto.tagging.CountryGroup;
import com.dropchop.recyclone.model.entity.jpa.security.ELoginAccount;
import com.dropchop.recyclone.model.entity.jpa.security.ETokenAccount;
import com.dropchop.recyclone.model.entity.jpa.tagging.ELanguageGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.ECountryGroup;
import com.dropchop.recyclone.rest.jaxrs.serialization.ObjectMapperFactory;
import com.dropchop.recyclone.service.api.mapping.DefaultPolymorphicRegistry;
import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
@ApplicationScoped
public class TestApplicationConfiguration {

  @Inject
  ObjectMapperFactory objectMapperFactory;

  @Produces
  ObjectMapper getObjectMapper() {
    return objectMapperFactory.createObjectMapper();
  }

  @Produces
  PolymorphicRegistry getPolymorphicRegistry() {
    return new DefaultPolymorphicRegistry()
      .registerDtoEntityMapping(LanguageGroup.class, ELanguageGroup.class)
      .registerDtoEntityMapping(CountryGroup.class, ECountryGroup.class)
      .registerDtoEntityMapping(LoginAccount.class, ELoginAccount.class)
      .registerDtoEntityMapping(TokenAccount.class, ETokenAccount.class)
      .registerSerializationConfig(new PolymorphicRegistry
        .SerializationConfig()
          .addSubType("LanguageGroup", LanguageGroup.class)
          .addSubType("CountryGroup", CountryGroup.class)
          .addSubType("LoginAccount", LoginAccount.class)
          .addSubType("TokenAccount", TokenAccount.class)
      );
  }
}
