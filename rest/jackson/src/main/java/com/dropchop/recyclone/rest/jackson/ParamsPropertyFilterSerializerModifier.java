package com.dropchop.recyclone.rest.jackson;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
public class ParamsPropertyFilterSerializerModifier extends BeanSerializerModifier {

  private final Params params;

  public ParamsPropertyFilterSerializerModifier() {
    this.params = null;
  }

  public ParamsPropertyFilterSerializerModifier(Params params) {
    this.params = params;
  }

  @Override
  public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                            BeanDescription beanDesc,
                                            JsonSerializer<?> serializer) {
    @SuppressWarnings("unchecked")
    JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
    return new ParamsPropertyFilterSerializer(jsonSerializer, params);
  }
}
