package com.dropchop.recyclone.rest.jaxrs.serialization;

import com.dropchop.recyclone.model.api.attr.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 02. 22.
 */
public class AttributeDeserializer extends JsonDeserializer<Attribute<?>> {

  private Attribute<?> parseAttribute(JsonNode node) throws IOException {
    JsonNode nameNode = node.get("name");
    if (nameNode == null) {
      throw new IOException("Attribute node is missing name property!");
    }
    String name = nameNode.asText();
    if (name == null || name.isBlank()) {
      throw new IOException("Attribute node has empty name property!");
    }
    if (!nameNode.isTextual()) {
      throw new IOException("Attribute node name property must be text!");
    }

    JsonNode valueNode = node.get("value");
    if (valueNode == null) {
      throw new IOException("Attribute node is missing value property!");
    }
    if (valueNode.isObject()) {
      throw new IOException("Attribute value property can not be object!");
    }

    if (valueNode.isArray()) {
      if (valueNode.size() == 0) {
        throw new IOException("Attribute value property can not empty array!");
      }
      Set<Attribute<?>> attributeSet = new LinkedHashSet<>();
      AttributeSet attributes = new AttributeSet();
      attributes.setName(name);
      attributes.setValue(attributeSet);
      for (int i = 0; i < valueNode.size(); i++) {
        attributeSet.add(parseAttribute(valueNode.get(i)));
      }
      return attributes;
    }

    String value = valueNode.asText();
    if (value == null || value.isBlank()) {
      throw new IOException("Attribute node has empty value property!");
    }

    if (valueNode.isBoolean()) {
      return new AttributeBool(name, valueNode.asBoolean());
    }

    if (valueNode.isFloatingPointNumber() || valueNode.isIntegralNumber()) {
      return new AttributeDecimal(name, valueNode.decimalValue());
    }

    AttributeDate attributeDate = AttributeDate.parseFromIsoString(name, value);
    if (attributeDate != null) {
      return attributeDate;
    }

    return new AttributeString(name, value);
  }

  @Override
  public Attribute<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);
    return parseAttribute(node);
  }

}
