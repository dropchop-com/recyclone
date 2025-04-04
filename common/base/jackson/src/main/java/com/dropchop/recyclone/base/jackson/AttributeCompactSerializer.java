package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.attr.AttributeToRemove;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Serializes any Attribute class to JSON form {"propName": "propValue"}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 08. 22.
 */
@Slf4j
public class AttributeCompactSerializer extends AttributeClassicSerializer {

  protected void nameValueWrite(JsonGenerator gen, Attribute<?> attribute, String name, Object value) throws IOException {
    if (value == null && !(attribute instanceof AttributeToRemove)) {
      return;
    }
    if (name == null) {
      return;
    }
    gen.writeStartObject(attribute);
    gen.writeFieldName(name);
    valueWrite(gen, value);
    gen.writeEndObject();
  }
}
