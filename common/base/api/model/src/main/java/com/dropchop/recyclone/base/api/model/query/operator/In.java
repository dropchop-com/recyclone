package com.dropchop.recyclone.base.api.model.query.operator;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public class In<T> extends SetValueOperator<T> {
  public In() {
  }

  public In(Collection<T> values) {
    super(values);
  }

  public Collection<T> get$in() {
    return super.getValues();
  }

  public void set$in(Collection<T> subConditions) {
    super.setValues(subConditions);
  }
}
