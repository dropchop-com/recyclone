package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.rest.jaxrs.serialization.ObjectMapperFactory;
import com.dropchop.recyclone.service.api.mapping.PolymorphicRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Jackson mapper factory which supports {@link Attribute} deserialization.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 02. 22.
 */
@Slf4j
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

  private final ObjectMapper mapper;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  public ObjectMapperContextResolver(PolymorphicRegistry polymorphicRegistry) {
    ObjectMapperFactory factory = new ObjectMapperFactory();
    this.mapper = factory.createObjectMapper(polymorphicRegistry);
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return mapper;
  }


}
