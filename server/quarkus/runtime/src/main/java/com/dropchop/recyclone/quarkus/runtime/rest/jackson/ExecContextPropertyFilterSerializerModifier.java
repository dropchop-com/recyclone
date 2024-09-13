package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 08. 22.
 */
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
