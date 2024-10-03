package com.dropchop.recyclone.model.api.query.operator;

import com.dropchop.recyclone.model.api.query.ConditionOperator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public abstract class UnaryValueOperator<T> implements ConditionOperator {

  private T value;

  public UnaryValueOperator() {
  }

  public UnaryValueOperator(T value) {
    this.value = value;
  }

  protected T getValue() {
    return value;
  }

  protected void setValue(T value) {
    this.value = value;
  }
}
