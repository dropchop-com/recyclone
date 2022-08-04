package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.model.dto.tagging.CountryGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.ELanguageGroup;
import com.dropchop.recyclone.model.entity.jpa.tagging.ECountryGroup;
import com.dropchop.recyclone.service.api.mapping.DefaultPolymorphicRegistry;
import com.dropchop.recyclone.service.api.mapping.PolymorphicRegistry;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
@ApplicationScoped
public class TestApplicationConfiguration {

  @Produces
  PolymorphicRegistry getPolymorphicRegistry() {
    return new DefaultPolymorphicRegistry()
      .registerDtoEntityMapping(LanguageGroup.class, ELanguageGroup.class)
      .registerDtoEntityMapping(CountryGroup.class, ECountryGroup.class)
      .registerSerializationConfig(new PolymorphicRegistry
        .SerializationConfig()
          .addSubType("LanguageGroup", LanguageGroup.class)
          .addSubType("CountryGroup", CountryGroup.class)
      );
  }
}
