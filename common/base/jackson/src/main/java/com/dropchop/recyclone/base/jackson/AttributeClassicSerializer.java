package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
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
@SuppressWarnings("rawtypes")
public class AttributeClassicSerializer extends JsonSerializer<Attribute> {

  protected void valueWrite(JsonGenerator gen, Object value) throws IOException {
    switch (value) {
      case null -> gen.writeNull();
      case String s -> gen.writeString(s);
      case Boolean b -> gen.writeBoolean(b);
      case ZonedDateTime zonedDateTime ->
          gen.writeString(Iso8601.DATE_TIME_MS_TZ_FORMATTER.get().format(zonedDateTime));
      case BigDecimal bigDecimal -> gen.writeNumber(bigDecimal);
      case List<?> objects -> {
        gen.writeStartArray(value);
        for (Object item : objects) {
          valueWrite(gen, item);
        }
        gen.writeEndArray();
      }
      case Set<?> objects -> {
        gen.writeStartObject(value);
        for (Object item : objects) {
          if (item instanceof Attribute<?> attr) {
            nameValueWrite(gen, attr, attr.getName(), attr.getValue());
          } else {
            log.warn("Item [{}] in set is not instance of [{}]. Skipping serialization!",
                item, Attribute.class.getName());
          }
        }
        gen.writeEndObject();
      }
      default -> {
      }
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
