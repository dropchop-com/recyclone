package com.dropchop.recyclone.model.api.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public class ConditionedField extends Field<ConditionOperator> {
  public ConditionedField() {
  }

  public ConditionedField(String name) {
    super(name);
  }

  public ConditionedField(String name, ConditionOperator val) {
    super(name, val);
  }
}
