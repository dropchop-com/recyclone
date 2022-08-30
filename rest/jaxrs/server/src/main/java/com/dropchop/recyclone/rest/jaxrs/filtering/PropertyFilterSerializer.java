package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContext;
import com.dropchop.recyclone.service.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.service.api.invoke.ExecContextProviderProducer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

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

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    ExecContextProvider ctxProvider = providerProducer
      .getFirstInitializedExecContextProvider();
    CommonParams params = null;
    if (ctxProvider != null) {
      ExecContext<?> execContext = ctxProvider.produce();
      if (execContext instanceof ParamsExecContext<?> paramsExecContext) {
        Params p = paramsExecContext.getParams();
        if (p instanceof CommonParams) {
          params = (CommonParams) p;
        }
      }
    }

    if (params != null && !(generator instanceof PropertyFilteringJsonGenerator)) {
      /*log.error("Created PropertyFilteringJsonGenerator [{}] with [{}]", o, params);
      Set<String> excludesIncludes = new LinkedHashSet<>();
      excludesIncludes.addAll(params.getContentIncludes());
      excludesIncludes.addAll(params.getContentExcludes().stream().map(s -> s = "-" + s).toList());
      generator = new PropertyFilteringJsonGenerator(
        generator, params.getContentTreeLevel(), params.getContentDetailLevel(), new PropertyFilter(excludesIncludes)
      );*/
    }
    delegate.serialize(o, generator, provider);
  }
}
