package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 08. 22.
 */
public class ExecContextPropertyFilterSerializer extends ParamsPropertyFilterSerializer {

  private final ExecContextContainer execContextContainer;

  public ExecContextPropertyFilterSerializer(JsonSerializer<Object> delegate,
                                             ExecContextContainer execContextContainer) {
    super(delegate, null);
    this.execContextContainer = execContextContainer;
  }

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    Params params = null;
    if (execContextContainer != null) {
      ExecContext<?> execContext = execContextContainer.get();
      if (execContext != null) {
        params = execContext.tryGetParams();
      }
    }

    super.serialize(params, o, generator, provider);
  }
}
