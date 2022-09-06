package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.StatusMessage;
import com.dropchop.recyclone.model.api.rest.Result;
import com.dropchop.recyclone.model.api.rest.ResultStats;
import com.dropchop.recyclone.model.api.rest.ResultStatus;
import com.dropchop.recyclone.model.dto.filtering.FieldFilter;
import com.dropchop.recyclone.service.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.service.api.invoke.ExecContextProviderProducer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 08. 22.
 */
@Slf4j
public class PropertyFilterSerializer extends StdSerializer<Object> {

  private final ExecContextProviderProducer providerProducer;
  private final JsonSerializer<Object> delegate;

  public PropertyFilterSerializer(JsonSerializer<Object> delegate,
                                  ExecContextProviderProducer execContextProviderProducer) {
    //noinspection unchecked
    super(delegate.handledType());
    this.delegate = delegate;
    this.providerProducer = execContextProviderProducer;
  }

  private boolean wrappedResult(Object o) {
    return o instanceof Result<?,?,?> ||
      o instanceof ResultStatus<?> ||
      o instanceof ResultStats ||
      o instanceof StatusMessage;
  }

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    ExecContextProvider ctxProvider = providerProducer
      .getFirstInitializedExecContextProvider();
    Params params = null;
    if (ctxProvider != null) {
      ExecContext<?> execContext = ctxProvider.produce();
      params = execContext.tryGetParams();
    }

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
}
