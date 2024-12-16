package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.filtering.FieldFilter;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 09. 22.
 */
@SuppressWarnings("unused")
public class
PropertyFilterSerializerModifier extends BeanSerializerModifier {

  private final FieldFilter filter;

  public PropertyFilterSerializerModifier() {
    this.filter = null;
  }

  public PropertyFilterSerializerModifier(FieldFilter filter) {
    this.filter = filter;
  }

  @Override
  public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                            BeanDescription beanDesc,
                                            JsonSerializer<?> serializer) {
    @SuppressWarnings("unchecked")
    JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
    return new PropertyFilterSerializer(jsonSerializer, filter);
  }
}
