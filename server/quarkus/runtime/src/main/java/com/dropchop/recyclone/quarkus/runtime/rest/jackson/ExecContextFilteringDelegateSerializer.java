package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.quarkus.arc.Arc;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.Context;

import java.io.IOException;

public interface ExecContextFilteringDelegateSerializer extends ParamsFilteringDelegateSerializer {

  ExecContextContainer getExecContextContainer();

  default boolean isRequestContextActive() {
    Context context = Arc.container().getActiveContext(RequestScoped.class);
    return context != null && context.isActive();
  }

  @Override
  default void serialize(Object o, JsonGenerator generator, SerializerProvider provider) throws IOException {
    if (!isRequestContextActive()) {
      //log.debug("Not request context");
      ParamsFilteringDelegateSerializer.super.serialize(o, generator, provider);
      return;
    }
    Params params = null;
    if (getExecContextContainer() != null) {
      ExecContext<?> execContext = getExecContextContainer().get();
      if (execContext != null) {
        params = execContext.tryGetParams();
      }
    }

    ParamsFilteringDelegateSerializer.super.serialize(params, o, generator, provider);
  }
}
