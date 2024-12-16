package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.ZonedDateTime;

public class ValueParser {

  public static Object parse(JsonNode valueNode) throws IOException {
    if (valueNode == null) {
      throw new IOException("Attribute node is missing value property!");
    }
    if (valueNode.isObject()) {
      throw new IOException("Attribute value property can not be object!");
    }
    String value = valueNode.asText();
    if (value == null || value.isBlank()) {
      throw new IOException("Attribute node has empty value property!");
    }

    if (valueNode.isBoolean()) {
      return valueNode.asBoolean();
    } else if (valueNode.isNull()) {
      return null;
    } if (valueNode.isFloatingPointNumber() || valueNode.isIntegralNumber()) {
      return valueNode.decimalValue();
    }

    ZonedDateTime date = Iso8601.fromIso(value);
    if (date != null) {
      return date;
    }

    return value;
  }
}
