package com.dropchop.recyclone.rest.jackson.server;

import com.dropchop.recyclone.model.api.filtering.FieldFilter;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
public class ParamsPropertyFilterSerializer extends PropertyFilterSerializer {
  private final Params params;

  public ParamsPropertyFilterSerializer(JsonSerializer<Object> delegate,
                                        Params params) {
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
