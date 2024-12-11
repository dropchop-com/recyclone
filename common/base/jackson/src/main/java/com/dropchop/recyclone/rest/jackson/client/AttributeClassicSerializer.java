package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.utils.Iso8601;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Serializes any Attribute class to JSON form {"name": "propName", "value": "propValue"}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 08. 22.
 */
@Slf4j
public class AttributeClassicSerializer extends JsonSerializer<Attribute> {

  protected void valueWrite(JsonGenerator gen, Object value) throws IOException {
    if (value == null) {
      gen.writeNull();
    } else if (value instanceof String) {
      gen.writeString((String) value);
    } else if (value instanceof Boolean) {
      gen.writeBoolean((Boolean) value);
    } else if (value instanceof ZonedDateTime) {
      gen.writeString(Iso8601.DATE_TIME_MS_TZ_FORMATTER.get().format((ZonedDateTime) value));
    } else if (value instanceof BigDecimal) {
      gen.writeNumber((BigDecimal) value);
    } else if (value instanceof List<?>) {
      gen.writeStartArray(value);
      for (Object item : (List<?>)value) {
        valueWrite(gen, item);
      }
      gen.writeEndArray();
    } else if (value instanceof Set<?>) {
      gen.writeStartObject(value);
      for (Object item : (Set<?>)value) {
        if (item instanceof Attribute<?> attr) {
          nameValueWrite(gen, attr, attr.getName(), attr.getValue());
        } else {
          log.warn("Item [{}] in set is not instance of [{}]. Skipping serialization!",
            item, Attribute.class.getName());
        }
      }
      gen.writeEndObject();
    }
  }

  protected void nameValueWrite(JsonGenerator gen, Attribute<?> attribute, String name, Object value) throws IOException {
    if (value == null) {
      return;
    }
    if (name == null) {
      return;
    }
    gen.writeStartObject(attribute);
    gen.writeFieldName("name");
    gen.writeString(name);
    gen.writeFieldName("value");
    valueWrite(gen, value);
    gen.writeEndObject();
  }

  @Override
  public Class<Attribute> handledType() {
    return Attribute.class;
  }

  @Override
  public void serialize(Attribute attribute, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (attribute == null) {
      return;
    }
    nameValueWrite(gen, attribute, attribute.getName(), attribute.getValue());
  }
}
