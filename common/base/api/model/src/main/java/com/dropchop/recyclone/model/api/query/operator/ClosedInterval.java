package com.dropchop.recyclone.model.api.query.operator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public class ClosedInterval<T> extends BinaryValueOperator<T> {

  public ClosedInterval() {
  }

  public ClosedInterval(T value1, T value2) {
    super(value1, value2);
  }

  public T get$gte() {
    return super.getValue1();
  }

  public void set$gte(T value1) {
    super.setValue1(value1);
  }

  public T get$lte() {
    return super.getValue2();
  }

  public void set$lte(T value2) {
    super.setValue2(value2);
  }
}
