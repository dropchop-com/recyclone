package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
public class TestPropertyFilterSerializerModifier extends PropertyFilterSerializerModifier {

  private final Params params;

  public TestPropertyFilterSerializerModifier(Params params) {
    this.params = params;
  }

  @Override
  public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                            BeanDescription beanDesc,
                                            JsonSerializer<?> serializer) {
    @SuppressWarnings("unchecked")
    JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
    return new TestPropertyFilterSerializer(jsonSerializer, params);
  }
}
