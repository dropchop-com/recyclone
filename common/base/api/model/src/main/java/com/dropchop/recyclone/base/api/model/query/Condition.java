package com.dropchop.recyclone.base.api.model.query;

import com.dropchop.recyclone.base.api.model.query.condition.And;
import com.dropchop.recyclone.base.api.model.query.condition.Not;
import com.dropchop.recyclone.base.api.model.query.condition.Or;
import com.dropchop.recyclone.base.api.model.query.operator.text.Phrase;
import com.dropchop.recyclone.base.api.model.query.operator.text.Wildcard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public interface Condition {

  static Map<String, Class<? extends Condition>> supported() {
    return Map.of(
      "and", And.class,
      "or", Or.class,
      "not", Not.class,
      "field", Field.class,
      "conditionedField", ConditionedField.class,
      "wildcard", Wildcard.class,
      "phrase", Phrase.class
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

  static <T> Wildcard<T> wildcard(T name, T value) {
    return new Wildcard<>(name, value);
  }

  static <T> Wildcard<T> wildcard(T name, T value, Boolean caseInsensitive) {
    return new Wildcard<>(name, value, caseInsensitive);
  }

  static <T> Wildcard<T> wildcard(T name, T value, Float boost) {
    return new Wildcard<>(name, value, boost);
  }

  static <T> Wildcard<T> wildcard(T name, T value, Boolean caseInsensitive, Float boost) {
    return new Wildcard<>(name, value, caseInsensitive, boost);
  }

  static <T> Phrase<T> phrase(T name, T value) {
    return new Phrase<>(name, value);
  }

  static <T> Phrase<T> phrase(T name, T value, Integer slop) {
    return new Phrase<>(name, value, slop);
  }

  static <T> Phrase<T> phrase(T name, T value, String analyzer) {
    return new Phrase<>(name, value, analyzer);
  }

  static <T> Phrase<T> phrase(T name, T value, String analyzer, Integer slop) {
    return new Phrase<>(name, value, analyzer, slop);
  }
}
