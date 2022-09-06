package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.filtering.FieldFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
public class TestPropertyFilterSerializer extends StdSerializer<Object> {

  private static final Logger log = LoggerFactory.getLogger(TestPropertyFilterSerializer.class);
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
    boolean start = false;
    if (params != null && !(generator instanceof FilteringJsonGenerator)) {
      start = true;
      generator = new FilteringJsonGenerator(
        generator, FieldFilter.fromParams(params)
      );
    }

    if (generator instanceof FilteringJsonGenerator filteringJsonGenerator) {
      if (filteringJsonGenerator.continueSerialization(o)) {
        delegate.serialize(o, generator, provider);
        if (start) { // end of root object serialization
          // actually write the JSON with saved generator write state commands.
          filteringJsonGenerator.outputDelayedWriteState();
        }
      }
    } else {
      delegate.serialize(o, generator, provider);
    }
  }
}
