package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Serializes any Attribute class to JSON form {"propName": "propValue"}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 08. 22.
 */
@Slf4j
public class AttributeCompactSerializer extends AttributeClassicSerializer {

  protected void nameValueWrite(JsonGenerator gen, String name, Object value) throws IOException {
    if (value == null) {
      return;
    }
    if (name == null) {
      return;
    }
    gen.writeStartObject();
    gen.writeFieldName(name);
    valueWrite(gen, value);
    gen.writeEndObject();
  }
}
