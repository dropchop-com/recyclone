package com.dropchop.recyclone.model.api.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class Gte<T> extends UnaryValueOperator<T> {

  public Gte() {
  }

  public Gte(T value) {
    super(value);
  }

  public T get$gte() {
    return super.getValue();
  }

  public void set$gte(T value) {
    super.setValue(value);
  }
}
