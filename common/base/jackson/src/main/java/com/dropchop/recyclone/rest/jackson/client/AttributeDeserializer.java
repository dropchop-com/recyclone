package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.base.api.model.attr.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;


/**
 * Deserializes JSON form to Attribute class where JSON can be
 * {"name": "propName", "value": "propValue"} or {"propName": "propValue"}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 02. 22.
 */
public class AttributeDeserializer extends JsonDeserializer<Attribute<?>> {

  private Attribute<?> parseAttribute(String name, JsonNode valueNode) throws IOException {
    if (valueNode.isArray()) {
      if (valueNode.isEmpty()) {
        throw new IOException("Attribute value property can not empty array!");
      }
      JsonNode itemNode = valueNode.get(0);
      Attribute<?> attr;
      if (itemNode.isObject()) {
        Set<Attribute<?>> tmp = new LinkedHashSet<>();
        attr = new AttributeSet(name, tmp);
        for (int i = 0; i < valueNode.size(); i++) {
          tmp.add(parseAttribute(valueNode.get(i)));
        }
      } else {
        List<Object> tmp = new ArrayList<>(valueNode.size());
        attr = new AttributeValueList<>(name, tmp);
        for (int i = 0; i < valueNode.size(); i++) {
          tmp.add(ValueParser.parse(valueNode.get(i)));
        }
      }
      return attr;
    }

    if (valueNode.isNull()) {
      return new AttributeToRemove(name);
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

  private Attribute<?> parseAttribute(JsonNode node) throws IOException {
    Map<String, JsonNode> fields = new HashMap<>();
    for (Iterator<Map.Entry<String, JsonNode>> iterator = node.fields(); iterator.hasNext();) {
      Map.Entry<String, JsonNode> entry = iterator.next();
      String name = entry.getKey();
      JsonNode value = entry.getValue();
      fields.put(name, value);
    }

    if (fields.size() == 1) { // {"propName": "propValue"}
      Map.Entry<String, JsonNode> nameValue = fields.entrySet().iterator().next();
      return parseAttribute(nameValue.getKey(), nameValue.getValue());
    } else if (fields.size() == 2) { // {"name": "propName", "value": "propValue"}
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
      return parseAttribute(name, valueNode);
    } else {
      throw new IOException("Unable to parse Attribute!");
    }
  }

  @Override
  public Attribute<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);
    return parseAttribute(node);
  }
}
