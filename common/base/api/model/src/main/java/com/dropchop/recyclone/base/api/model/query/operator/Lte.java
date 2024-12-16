package com.dropchop.recyclone.base.api.model.query.operator;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class Lte<T> extends UnaryValueOperator<T> {

  public Lte() {
  }

  public Lte(T value) {
    super(value);
  }

  public T get$lte() {
    return super.getValue();
  }

  public void set$lte(T value) {
    super.setValue(value);
  }
}
