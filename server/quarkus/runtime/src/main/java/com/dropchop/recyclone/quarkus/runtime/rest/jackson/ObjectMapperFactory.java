package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * CDI Capable ObjectMapperFactory extension.
 * Usage: Inject it and wrap createObjectMapper() in Producer method to customize ObjectMapper.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 23. 06. 22.
 */
@ApplicationScoped
public class ObjectMapperFactory extends com.dropchop.recyclone.rest.jackson.server.ObjectMapperFactory {

  @Inject
  public ObjectMapperFactory(JsonSerializationTypeConfig serializationTypeConfig,
                             BeanSerializerModifier serializerModifier,
                             BeanDeserializerModifier deserializerModifier) {
    super(serializationTypeConfig, serializerModifier, deserializerModifier);
  }

  @Override
  public ObjectMapper createObjectMapper() {
    return super.createObjectMapper();
  }
}
