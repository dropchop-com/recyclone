package com.dropchop.recyclone.rest.jackson.server;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
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
      params = execContext.tryGetParams();
    }

    super.serialize(params, o, generator, provider);
  }
}
