package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * CDI Capable ObjectMapperFactory extension.
 * Usage: Inject it and wrap createObjectMapper() in Producer method to customize ObjectMapper.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 23. 06. 22.
 */
@Slf4j
@ApplicationScoped
public class ObjectMapperFactory extends com.dropchop.recyclone.rest.jackson.server.ObjectMapperFactory {

  @Override
  public ObjectMapper createObjectMapper() {
    return super.createObjectMapper();
  }

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  public ObjectMapperFactory(JsonSerializationTypeConfig serializationTypeConfig,
                             BeanSerializerModifier serializerModifier) {
    super(serializationTypeConfig, serializerModifier);
  }
}
