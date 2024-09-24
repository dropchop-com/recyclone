package com.dropchop.recyclone.model.api.query;

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
        "conditionedField", ConditionedField.class
    );
  }

  static And and(Condition ... conditions) {
    return new And(new ArrayList<>(Arrays.asList(conditions)));
  }

  static And and(List<Condition> conditions) {
    return new And(conditions);
  }

  static Or or(Condition ... conditions) {
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
}
