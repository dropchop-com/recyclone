package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.quarkus.arc.Arc;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public interface ExecContextFilteringDelegateSerializer extends ParamsFilteringDelegateSerializer {

  Logger log = LoggerFactory.getLogger(ExecContextFilteringDelegateSerializer.class);

  default boolean isRequestContextActive() {
    Context context = Arc.container().getActiveContext(RequestScoped.class);
    return context != null && context.isActive();
  }

  default void serialize(ExecContextContainer execContextContainer, Object o, JsonGenerator generator,
                         SerializerProvider provider) throws IOException {
    if (!isRequestContextActive()) {
      log.debug("Not request context");
      ParamsFilteringDelegateSerializer.super.serialize((Params) null, o, generator, provider);
      return;
    }
    Params params = null;
    if (execContextContainer != null) {
      ExecContext<?> execContext = execContextContainer.get();
      if (execContext != null) {
        params = execContext.tryGetParams();
      }
    }

    ParamsFilteringDelegateSerializer.super.serialize(params, o, generator, provider);
  }
}
