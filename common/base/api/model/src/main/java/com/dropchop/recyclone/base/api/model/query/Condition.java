package com.dropchop.recyclone.base.api.model.query;

import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.dropchop.recyclone.base.api.model.query.knn.Knn;
import com.dropchop.recyclone.base.api.model.query.operator.Match;
import com.dropchop.recyclone.base.api.model.query.operator.text.AdvancedText;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public interface Condition {

  static Map<String, Class<? extends Condition>> supported() {
    return Map.of(
      "and", And.class,
      "or", Or.class,
      "not", Not.class,
      "field", Field.class,
      "conditionedField", ConditionedField.class,
      "knn", Knn.class
    );
  }

  static And and(Condition... conditions) {
    return new And(new ArrayList<>(Arrays.asList(conditions)));
  }

  static And and(List<Condition> conditions) {
    return new And(conditions);
  }

  static Or or(Condition... conditions) {
    return new Or(new ArrayList<>(Arrays.asList(conditions)));
  }

  static Or or(List<Condition> conditions) {
    return new Or(conditions);
  }

  static Not not(Condition condition) {
    return new Not(condition);
  }

  static ConditionedField field(String name, ConditionOperator operator) {
    return new ConditionedField(name, operator);
  }

  static <T> Field<T> field(String name, T value) {
    return new Field<>(name, value);
  }

  static Knn knn(String field, float[] queryVector, Integer k) {
    return Knn.of(field, queryVector, k);
  }

  static Knn knn(String field, float[] queryVector, Integer k, Condition filter) {
    return Knn.of(field, queryVector, k, filter);
  }

  static Knn knn(String field, float[] queryVector, Integer k, Condition filter, Float similarity) {
    return Knn.of(field, queryVector, k, filter, similarity);
  }

  static ConditionedField advancedText(String name, String value) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value)));
  }

  static ConditionedField advancedText(String name, String value, Integer slop) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, slop)));
  }

  static ConditionedField advancedText(String name, String value, Boolean caseInsensitive) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, caseInsensitive)));
  }

  static ConditionedField advancedText(String name, String value, Boolean caseInsensitive, Integer slop) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, caseInsensitive, slop)));
  }

  static ConditionedField advancedText(String name, String value, Boolean caseInsensitive, Integer slop, Boolean inOrder) {
    return new ConditionedField(name, new Match<>(new AdvancedText(value, caseInsensitive, slop, inOrder)));
  }

  static ConditionedField phrase(String name, String value) {
    return new ConditionedField(name, new Match<>(new Phrase(value)));
  }

  static ConditionedField phrase(String name, String value, Integer slop) {
    return new ConditionedField(name, new Match<>(new Phrase(value, slop)));
  }

  static ConditionedField phrase(String name, String value, String analyzer) {
    return new ConditionedField(name, new Match<>(new Phrase(value, analyzer)));
  }

  static ConditionedField phrase(String name, String value, String analyzer, Integer slop) {
    return new ConditionedField(name, new Match<>(new Phrase(value, analyzer, slop)));
  }

  static ConditionedField wildcard(String name, String value) {
    return new ConditionedField(name, new Match<>(new Wildcard(value)));
  }

  static ConditionedField wildcard(String name, String value, Boolean caseInsensitive) {
    return new ConditionedField(name, new Match<>(new Wildcard(value, caseInsensitive)));
  }

  static ConditionedField wildcard(String name, String value, Float boost) {
    return new ConditionedField(name, new Match<>(new Wildcard(value, boost)));
  }

  static ConditionedField wildcard(String name, String value, Boolean caseInsensitive, Float boost) {
    return new ConditionedField(name, new Match<>(new Wildcard(value, caseInsensitive, boost)));
  }
}
