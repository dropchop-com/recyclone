package com.dropchop.recyclone.model.api.query.operator;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class Gt<T> extends UnaryValueOperator<T> {

  public Gt() {
  }

  public Gt(T value) {
    super(value);
  }

  public T get$gt() {
    return super.getValue();
  }

  public void set$gt(T value) {
    super.setValue(value);
  }
}
