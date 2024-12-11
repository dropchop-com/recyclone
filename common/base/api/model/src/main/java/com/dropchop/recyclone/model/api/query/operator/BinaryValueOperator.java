package com.dropchop.recyclone.model.api.query.operator;

import com.dropchop.recyclone.model.api.query.ConditionOperator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public abstract class BinaryValueOperator<T> implements ConditionOperator {

  private T value1;
  private T value2;

  public BinaryValueOperator() {
  }

  public BinaryValueOperator(T value1, T value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  protected T getValue1() {
    return value1;
  }

  protected void setValue1(T value1) {
    this.value1 = value1;
  }

  protected T getValue2() {
    return value2;
  }

  protected void setValue2(T value2) {
    this.value2 = value2;
  }
}
