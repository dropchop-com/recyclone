package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/27/25.
 */
@RegisterForReflection
public class DefaultObjectMapper extends ObjectMapper {

  public DefaultObjectMapper() {
  }

  public DefaultObjectMapper(JsonFactory jf) {
    super(jf);
  }

  public DefaultObjectMapper(ObjectMapper src) {
    super(src);
  }

  public DefaultObjectMapper(ObjectMapper src, JsonFactory factory) {
    super(src, factory);
  }

  public DefaultObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc) {
    super(jf, sp, dc);
  }

  @Override
  public ObjectMapper copy() {
    _checkInvalidCopy(this.getClass());
    return new DefaultObjectMapper(this);
  }
}
