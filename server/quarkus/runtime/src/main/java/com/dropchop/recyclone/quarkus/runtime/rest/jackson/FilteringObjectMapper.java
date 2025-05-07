package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/27/25.
 */
@RegisterForReflection
@SuppressWarnings("unused")
public class FilteringObjectMapper extends ObjectMapper {

  public FilteringObjectMapper() {
  }

  public FilteringObjectMapper(ObjectMapper src) {
    super(src);
  }

  public FilteringObjectMapper(ObjectMapper src, JsonFactory factory) {
    super(src, factory);
  }

  @Override
  public ObjectMapper copy() {
    return new FilteringObjectMapper(this);
  }

  @Override
  public ObjectMapper copyWith(JsonFactory factory) {
    return new FilteringObjectMapper(this, factory);
  }
}
