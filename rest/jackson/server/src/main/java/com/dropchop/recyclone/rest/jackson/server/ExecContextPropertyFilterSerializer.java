package com.dropchop.recyclone.rest.jackson.server;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainerProvider;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 08. 22.
 */
@Slf4j
public class ExecContextPropertyFilterSerializer extends ParamsPropertyFilterSerializer {

  private final ExecContextContainerProvider providerProducer;

  public ExecContextPropertyFilterSerializer(JsonSerializer<Object> delegate,
                                             ExecContextContainerProvider execContextProviderProducer) {
    super(delegate, null);
    this.providerProducer = execContextProviderProducer;
  }

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    ExecContextContainer ctxProvider = providerProducer
      .getFirstInitializedExecContextProvider();
    Params params = null;
    if (ctxProvider != null) {
      ExecContext<?> execContext = ctxProvider.get();
      params = execContext.tryGetParams();
    }

    super.serialize(params, o, generator, provider);
  }
}
