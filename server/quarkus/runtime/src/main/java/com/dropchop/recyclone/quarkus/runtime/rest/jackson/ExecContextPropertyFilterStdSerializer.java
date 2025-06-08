package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.quarkus.arc.Arc;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 08. 22.
 */
public class ExecContextPropertyFilterStdSerializer extends ParamsPropertyFilterStdSerializer {

  private static final Logger log = LoggerFactory.getLogger(ExecContextPropertyFilterStdSerializer.class);
  private final ExecContextContainer execContextContainer;

  public ExecContextPropertyFilterStdSerializer(JsonSerializer<Object> delegate,
                                                ExecContextContainer execContextContainer) {
    super(delegate, null);
    this.execContextContainer = execContextContainer;
  }

  public boolean isRequestContextActive() {
    Context context = Arc.container().getActiveContext(RequestScoped.class);
    return context != null && context.isActive();
  }

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider) throws IOException {
    if (!isRequestContextActive()) {
      log.debug("Not request context");
      super.serialize(o, generator, provider);
      return;
    }
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
