package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.filtering.FieldFilter;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public interface ParamsFilteringDelegateSerializer extends FilteringDelegateSerializer {

  Params getParams();

  default void serialize(Params params, Object o, JsonGenerator generator, SerializerProvider provider)
      throws IOException {
    FilteringDelegateSerializer.super.serialize(
        params != null ? FieldFilter.fromParams(params) : null, o, generator, provider
    );
  }

  @Override
  default void serialize(Object o, JsonGenerator generator, SerializerProvider provider) throws IOException {
    this.serialize(getParams(), o, generator, provider);
  }
}
