package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.dropchop.recyclone.model.api.filtering.PolymorphicRegistry;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 23. 06. 22.
 */
@Slf4j
@ApplicationScoped
public class ObjectMapperFactory extends com.dropchop.recyclone.rest.jackson.ObjectMapperFactory {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  public ObjectMapperFactory(PolymorphicRegistry polymorphicRegistry,
                             BeanSerializerModifier serializerModifier) {
    super(polymorphicRegistry, serializerModifier);
  }
}
