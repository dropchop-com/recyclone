package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
public class TestPropertyFilterSerializer extends StdSerializer<Object> {

  private final JsonSerializer<Object> delegate;
  private final Params params;

  public TestPropertyFilterSerializer(JsonSerializer<Object> delegate,
                                  Params params) {
    //noinspection unchecked
    super(delegate.handledType());
    this.delegate = delegate;
    this.params = params;
  }

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    if (params != null && !(generator instanceof FilteringJsonGenerator)) {
      generator = new FilteringJsonGenerator(
        generator, new FilteringContext().setParams(params)
      );
    }
    delegate.serialize(o, generator, provider);
  }
}
