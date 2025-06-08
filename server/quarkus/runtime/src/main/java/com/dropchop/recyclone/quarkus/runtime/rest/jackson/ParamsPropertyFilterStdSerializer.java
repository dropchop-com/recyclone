package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.filtering.FieldFilter;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 09. 22.
 */
public class ParamsPropertyFilterStdSerializer extends PropertyFilterStdSerializer {
  private final Params params;

  public ParamsPropertyFilterStdSerializer(JsonSerializer<Object> delegate, Params params) {
    super(delegate, params != null ? FieldFilter.fromParams(params) : null);
    this.params = params;
  }

  protected void serialize(Params params, Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    super.serialize(
      params != null ? FieldFilter.fromParams(params) : null,
      o, generator, provider
    );
  }

  @Override
  public void serialize(Object o, JsonGenerator generator, SerializerProvider provider)
    throws IOException {
    this.serialize(params, o, generator, provider);
  }
}
