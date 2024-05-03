package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.rest.jackson.server.ExecContextPropertyFilterSerializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 08. 22.
 */
@Slf4j
@ApplicationScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class ExecContextPropertyFilterSerializerModifier extends BeanSerializerModifier {

  @Inject
  ExecContextContainer execContextContainer;

  @Override
  public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                            BeanDescription beanDesc,
                                            JsonSerializer<?> serializer) {
    @SuppressWarnings("unchecked")
    JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
    return new ExecContextPropertyFilterSerializer(jsonSerializer, execContextContainer);
  }
}
