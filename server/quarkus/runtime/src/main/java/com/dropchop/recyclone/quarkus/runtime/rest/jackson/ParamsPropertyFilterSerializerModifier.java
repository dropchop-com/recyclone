package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 09. 22.
 * @noinspection unused
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
    if (serializer instanceof BeanSerializer beanSerializer) {
      return new ParamsPropertyFilterBeanSerializer(beanSerializer, params);
    }
    //noinspection unchecked
    return new ParamsPropertyFilterStdSerializer(
        (JsonSerializer<Object>) serializer, params
    );
  }
}
