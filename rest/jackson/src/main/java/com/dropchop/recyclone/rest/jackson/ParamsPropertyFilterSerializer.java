package com.dropchop.recyclone.rest.jackson;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.filtering.FieldFilter;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.StatusMessage;
import com.dropchop.recyclone.model.api.rest.Result;
import com.dropchop.recyclone.model.api.rest.ResultStats;
import com.dropchop.recyclone.model.api.rest.ResultStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
public class ParamsPropertyFilterSerializer extends StdSerializer<Object> {
  private final JsonSerializer<Object> delegate;
  private final Params params;

  public ParamsPropertyFilterSerializer(JsonSerializer<Object> delegate,
                                        Params params) {
    //noinspection unchecked
    super(delegate.handledType());
    this.delegate = delegate;
    this.params = params;
  }

  private boolean wrappedResult(Object o) {
    return o instanceof Result<?,?,?> ||
      o instanceof ResultStatus<?> ||
      o instanceof ResultStats ||
      o instanceof StatusMessage;
  }

  protected void serialize(Params params, Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    boolean start = false;
    if (params != null && o instanceof Model && !wrappedResult(o) && !(generator instanceof FilteringJsonGenerator)) {
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

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    this.serialize(params, o, generator, provider);
  }
}
