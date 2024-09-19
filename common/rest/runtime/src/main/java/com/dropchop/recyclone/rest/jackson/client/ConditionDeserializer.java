package com.dropchop.recyclone.rest.jackson.client;

import com.dropchop.recyclone.model.api.query.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public class ConditionDeserializer extends JsonDeserializer<Condition> {

  private Condition parseConditionedField(String name, Map<String, JsonNode> fields) {
    return new ConditionedField(name, new Eq<>());
  }

  private Condition parseField(String name, JsonNode node) {
    if (node.isObject()) {
      Map<String, JsonNode> fields = new LinkedHashMap<>();
      int numOps = 0;
      for (Iterator<Map.Entry<String, JsonNode>> iterator = node.fields(); iterator.hasNext(); ) {
        Map.Entry<String, JsonNode> entry = iterator.next();
        String fname = entry.getKey();
        if (fname.startsWith("$")) {
          numOps++;
        }
        JsonNode value = entry.getValue();
        fields.put(fname, value);
      }
      if (numOps != fields.size()) {
        throw new IllegalArgumentException("Invalid query field at [" + name + "]!");
      }
      return parseConditionedField(name, fields);
    } else {
      if (node.isFloat()) {
        return new Field<>(name, node.asDouble());
      } else if (node.isTextual()) {
        return new Field<>(name, node.asText());
      } else if (node.isIntegralNumber()) {
        return new Field<>(name, node.asLong());
      } else if (node.isBoolean()) {
        return new Field<>(name, node.asBoolean());
      }
      throw new IllegalArgumentException("Invalid query field value at [" + name + "]!");
    }
  }

  private Condition parseCondition(String name, JsonNode valueNode) throws IOException {
    if (name.startsWith("$")) {
      Class<? extends Condition> cClass = Condition.supported().get(name.substring(1));
      if (cClass != null) {
        List<Condition> values = null;
        if (valueNode.isArray() && !valueNode.isEmpty()) {
          values = new ArrayList<>();
          for (int i = 0; i < valueNode.size(); i++) {
            JsonNode v = valueNode.get(i);
            String cname;
            if (v.isObject()) {
              cname = v.fieldNames().next();
            } else {
              throw new IllegalArgumentException("Invalid query child structure at [" + name + "]!");
            }
            values.add(parseCondition(cname, v));
          }
        }
        //noinspection IfCanBeSwitch
        if (cClass.equals(And.class)) {
          return new And(values);
        } else if (cClass.equals(Or.class)) {
          return new Or(values);
        } else if (cClass.equals(Not.class)) {
          if (values != null) {
            return new Not(values.getFirst());
          } else {
            return new Not();
          }
        } else {
          throw new UnsupportedOperationException("Missing condition implementation for [" + name + "]!");
        }
      }
    } else {
      return parseField(name, valueNode.get(name));
    }
    throw new IllegalArgumentException("Invalid query structure at [" + name + "]!");
  }

  @Override
  public Condition deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
    // the root of condition hierarchy
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);
    for (Iterator<Map.Entry<String, JsonNode>> iterator = node.fields(); iterator.hasNext();) {
      Map.Entry<String, JsonNode> entry = iterator.next();
      String name = entry.getKey();
      JsonNode value = entry.getValue();
      if (name.startsWith("$")) {
        return parseCondition(name, value);
      } else {
        return parseField(name, value);
      }
    }
    return null;
  }
}
