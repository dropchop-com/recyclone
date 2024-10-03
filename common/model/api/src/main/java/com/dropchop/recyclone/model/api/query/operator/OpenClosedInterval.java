package com.dropchop.recyclone.model.api.query.operator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public class OpenClosedInterval<T> extends BinaryValueOperator<T> {

  public OpenClosedInterval() {
  }

  public OpenClosedInterval(T value1, T value2) {
    super(value1, value2);
  }

  public T get$gt() {
    return super.getValue1();
  }

  public void set$gt(T value1) {
    super.setValue1(value1);
  }

  public T get$lte() {
    return super.getValue2();
  }

  public void set$lte(T value2) {
    super.setValue2(value2);
  }}
