package com.dropchop.recyclone.model.api.query.operator;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class Eq<T> extends UnaryValueOperator<T> {

  public Eq() {
  }

  public Eq(T value) {
    super(value);
  }

  public T get$eq() {
    return super.getValue();
  }

  public void set$eq(T value) {
    super.setValue(value);
  }
}
