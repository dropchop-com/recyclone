package com.dropchop.recyclone.model.api.query.operator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public class ClosedOpenInterval<T> extends BinaryValueOperator<T> {

  public ClosedOpenInterval() {
  }

  public ClosedOpenInterval(T value1, T value2) {
    super(value1, value2);
  }

  public T get$gte() {
    return super.getValue1();
  }

  public void set$gte(T value1) {
    super.setValue1(value1);
  }

  public T get$lt() {
    return super.getValue2();
  }

  public void set$lt(T value2) {
    super.setValue2(value2);
  }
}
