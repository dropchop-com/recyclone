package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.filtering.FieldFilter;
import com.dropchop.recyclone.model.api.invoke.StatusMessage;
import com.dropchop.recyclone.model.api.rest.Result;
import com.dropchop.recyclone.model.api.rest.ResultStats;
import com.dropchop.recyclone.model.api.rest.ResultStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
public class PropertyFilterSerializer extends StdSerializer<Object> {
  private final JsonSerializer<Object> delegate;
  private final FieldFilter filter;

  public PropertyFilterSerializer(JsonSerializer<Object> delegate,
                                  FieldFilter filter) {
    super(delegate.handledType());
    this.delegate = delegate;
    this.filter = filter;
  }

  private boolean wrappedResult(Object o) {
    return o instanceof Result<?,?,?> ||
      o instanceof ResultStatus<?> ||
      o instanceof ResultStats ||
      o instanceof StatusMessage;
  }

  protected void serialize(FieldFilter filter, Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    boolean start = false;
    if (filter != null && o instanceof Model && !wrappedResult(o) && !(generator instanceof FilteringJsonGenerator)) {
      start = true;
      generator = new FilteringJsonGenerator(
        generator, filter
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

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    this.serialize(filter, o, generator, provider);
  }

  @Override
  public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
    delegate.serializeWithType(value, gen, serializers, typeSer);
  }
}
