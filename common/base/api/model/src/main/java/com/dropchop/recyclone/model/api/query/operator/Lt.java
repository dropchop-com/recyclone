package com.dropchop.recyclone.model.api.query.operator;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class Lt<T> extends UnaryValueOperator<T> {

  public Lt() {
  }

  public Lt(T value) {
    super(value);
  }

  public T get$lt() {
    return super.getValue();
  }

  public void set$lt(T value) {
    super.setValue(value);
  }
}
