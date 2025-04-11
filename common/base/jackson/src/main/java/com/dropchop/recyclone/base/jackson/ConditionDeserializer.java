package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.ConditionOperator;
import com.dropchop.recyclone.base.api.model.query.ConditionedField;
import com.dropchop.recyclone.base.api.model.query.Field;
import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;
import com.dropchop.recyclone.base.api.model.query.operator.*;
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

  private Condition parseConditionedField(String name, Map<String, JsonNode> fields) throws IOException {
    if (fields.size() != 1 && fields.size() != 2) {
      throw new IllegalArgumentException("Invalid query conditioned field structure at [" + name + "]!");
    }
    List<Class<? extends ConditionOperator>> cClasses = new ArrayList<>();
    List<String> cNames = new ArrayList<>();
    for (Map.Entry<String, JsonNode> entry : fields.entrySet()) {
      String cname = entry.getKey();
      if (!cname.startsWith("$")) {
        throw new IllegalArgumentException(
            "Invalid query conditioned field name structure at [" + name + "." + cname + "]!"
        );
      }
      Class<? extends ConditionOperator> cClass = ConditionOperator.supported().get(cname.substring(1));
      if (cClass == null) {
        throw new UnsupportedOperationException(
            "Missing condition implementation for [" + name + "." + cname + "]!"
        );
      }
      cClasses.add(cClass);
      cNames.add(cname);
    }

    if (fields.size() == 2) {
      String cname1 = cNames.getFirst();
      String cname2 = cNames.getLast();
      if (cname1.startsWith("$lt") && cname2.startsWith("$gt")) { // swap values in list
        Collections.swap(cNames, 0, cNames.size() - 1);
        Collections.swap(cClasses, 0, cClasses.size() - 1);
      }
      JsonNode valueNode1 = fields.get(cNames.getFirst());
      Object value1 = ValueParser.parse(valueNode1);
      if (value1 == null) {
        throw new IllegalArgumentException("Invalid query field value at [" + name + "." + cNames.getFirst() + "]!");
      }
      JsonNode valueNode2 = fields.get(cNames.getLast());
      Object value2 = ValueParser.parse(valueNode2);
      if (value2 == null) {
        throw new IllegalArgumentException(
            "Invalid query second field value at [" + name + "." + cNames.getLast() + "]!"
        );
      }
      if (Gt.class.equals(cClasses.getFirst()) && Lt.class.equals(cClasses.getLast())) {
        return new ConditionedField(name, new OpenInterval<>(value1, value2));
      } else if (Gt.class.equals(cClasses.getFirst()) && Lte.class.equals(cClasses.getLast())) {
        return new ConditionedField(name, new OpenInterval<>(value1, value2));
      } else if (Gte.class.equals(cClasses.getFirst()) && Lt.class.equals(cClasses.getLast())) {
        return new ConditionedField(name, new OpenInterval<>(value1, value2));
      } else if (Gte.class.equals(cClasses.getFirst()) && Lte.class.equals(cClasses.getLast())) {
        return new ConditionedField(name, new OpenInterval<>(value1, value2));
      } else {
        throw new IllegalArgumentException(
            "Missing condition implementation for conditioned field operators [" + name + "." + cNames + "]!"
        );
      }
    } else {
      String operatorName = cNames.getFirst();
      JsonNode valueNode = fields.get(operatorName);
      if (Eq.class.equals(cClasses.getFirst())) {
        Object value1 = ValueParser.parse(valueNode);
        return new ConditionedField(name, new Eq<>(value1));
      } else if (Match.class.equals(cClasses.getFirst())) {
        if(!valueNode.isObject() || valueNode.isEmpty()) {
          throw new IllegalArgumentException("Invalid query structure at [" + name + "] value is missing or wrong!");
        }
        if (operatorName.endsWith("match")) {
          try {
            String conditionValue = valueNode.get("value").asText();
            Boolean caseInsensitive = valueNode.has("caseInsensitive")
                ? valueNode.get("caseInsensitive").asBoolean()
                : null;

            Integer slop = valueNode.has("slop")
                ? valueNode.get("slop").asInt()
                : null;

            Boolean inOrder = valueNode.has("inOrder")
                ? valueNode.get("inOrder").asBoolean()
                : null;

            return new ConditionedField(
                name, new Match<>(new AdvancedText(conditionValue, caseInsensitive, slop, inOrder))
            );
          } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid [%s] format at [%s]: %s".formatted(AdvancedText.class.getSimpleName(), name, e.getMessage()),
                e
            );
          }
        } else if (operatorName.endsWith("matchPhrase")) {
          try {
            String conditionValue = valueNode.get("value").asText();
            String analyzer = valueNode.has("analyzer")
                ? valueNode.get("analyzer").asText()
                : null;

            Integer slop = valueNode.has("slop")
                ? valueNode.get("slop").asInt()
                : null;

            return new ConditionedField(name, new Match<>(new Phrase(conditionValue, analyzer, slop)));
          } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid [%s] format at [%s]: %s".formatted(Phrase.class.getSimpleName(), name, e.getMessage()),
                e
            );
          }
        } else if (operatorName.endsWith("matchWildcard")) {
          try {
            String conditionValue = valueNode.get("value").asText();
            Boolean caseInsensitive = valueNode.has("caseInsensitive")
                ? valueNode.get("caseInsensitive").asBoolean()
                : null;

            Float boost = valueNode.has("boost")
                ? Double.valueOf(valueNode.get("boost").asDouble()).floatValue()
                : null;

            return new ConditionedField(name, new Match<>(new Wildcard(conditionValue, caseInsensitive, boost)));
          } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Invalid [%s] format at [%s]: %s".formatted(Wildcard.class.getSimpleName(), name, e.getMessage()),
                e
            );
          }
        } else {
          throw new IllegalArgumentException(
              "Invalid Match text format at [%s]".formatted(name)
          );
        }
      } else if (In.class.equals(cClasses.getFirst())) {
        List<Object> tmp = new ArrayList<>(valueNode.size());
        for (int i = 0; i < valueNode.size(); i++) {
          tmp.add(ValueParser.parse(valueNode.get(i)));
        }
        return new ConditionedField(name, new In<>(tmp));
      } else {
        throw new IllegalArgumentException(
            "Missing condition implementation for conditioned field operator [" + name + "." + cNames.getFirst() + "]!"
        );
      }
    }
  }

  private Condition parseField(String name, JsonNode node) throws IOException {
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
      Object o = ValueParser.parse(node);
      //if (o == null) {
      //  throw new IllegalArgumentException("Invalid query field value at [" + name + "]!");
      //}
      return new Field<>(name, o);
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
            values.add(parseCondition(cname, v.get(cname)));
          }
        }
        if (cClass.equals(And.class)) {
          return new And(values);
        } else if (cClass.equals(Or.class)) {
          return new Or(values);
        } else if (cClass.equals(Not.class)) {
          if (!valueNode.isObject() || valueNode.isEmpty() || valueNode.size() != 1) {
            throw new IllegalArgumentException("Invalid query structure at [" + name + "] value is missing or wrong!");
          }
          String cname = valueNode.fieldNames().next();
          Condition v = parseCondition(cname, valueNode.get(cname));
          return new Not(v);
        } else {
          throw new UnsupportedOperationException("Missing condition implementation for [" + name + "]!");
        }
      }
    } else {
      return parseField(name, valueNode);
    }
    throw new IllegalArgumentException("Invalid query structure at [" + name + "]!");
  }

  @Override
  public Condition deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
    // the root of condition hierarchy
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    if (iterator.hasNext()) {
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
