package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.dropchop.recyclone.rest.jaxrs.filtering.ExecContextPropertyFilterSerializerModifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 23. 06. 22.
 */
@Slf4j
@ApplicationScoped
public class ObjectMapperFactory extends com.dropchop.recyclone.rest.jackson.server.ObjectMapperFactory {

  @Inject
  PolymorphicRegistry polymorphicRegistry;

  @Inject
  ExecContextPropertyFilterSerializerModifier serializerModifier;

  @Override
  public ObjectMapper createObjectMapper() {
    this.setPolymorphicRegistry(polymorphicRegistry);
    this.setSerializerModifier(serializerModifier);
    return super.createObjectMapper();
  }
}
