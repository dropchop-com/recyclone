package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.filtering.FieldFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Standard serializer that delegates serialization to a {@link JsonSerializer}
 * and applies filtering.
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 09. 22.
 */
public class PropertyFilterStdSerializer extends StdSerializer<Object> implements FilteringDelegateSerializer {
  private final JsonSerializer<Object> delegate;
  private final FieldFilter filter;

  public PropertyFilterStdSerializer(JsonSerializer<Object> delegate, FieldFilter filter) {
    super(delegate.handledType());
    this.delegate = delegate;
    this.filter = filter;
  }

  public JsonSerializer<Object> getDelegate() {
    return delegate;
  }

  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider) throws IOException {
    this.serialize(filter, o, generator, provider);
  }

  public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers,
                                TypeSerializer typeSer) throws IOException {
    getDelegate().serializeWithType(value, gen, serializers, typeSer);
  }
}
