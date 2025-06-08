package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.filtering.FieldFilter;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.api.model.rest.Result;
import com.dropchop.recyclone.base.api.model.rest.ResultStats;
import com.dropchop.recyclone.base.api.model.rest.ResultStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

public interface FilteringDelegateSerializer {

  JsonSerializer<Object> getDelegate();
  FieldFilter getFilter();

  default boolean wrappedResult(Object o) {
    return o instanceof Result<?,?,?> ||
        o instanceof ResultStatus<?> ||
        o instanceof ResultStats ||
        o instanceof StatusMessage;
  }

  default void serialize(FieldFilter filter, Object o, JsonGenerator generator, SerializerProvider provider)
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
        getDelegate().serialize(o, generator, provider);
        if (start) { // end of root object serialization
          // actually write the JSON with saved generator write state commands.
          filteringJsonGenerator.outputDelayedWriteState();
        }
      }
    } else {
      getDelegate().serialize(o, generator, provider);
    }
  }

  default void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
      throws IOException {
    this.serialize(getFilter(), o, generator, provider);
  }

  default void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers,
                                 TypeSerializer typeSer) throws IOException {
    getDelegate().serializeWithType(value, gen, serializers, typeSer);
  }
}
