package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
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
    if (serializer instanceof BeanSerializer beanSerializer) {
      return new ExecContextPropertyFilterBeanSerializer(beanSerializer, execContextContainer);
    }
    //noinspection unchecked
    return new ExecContextPropertyFilterStdSerializer(
        (JsonSerializer<Object>) serializer, execContextContainer
    );
  }
}
