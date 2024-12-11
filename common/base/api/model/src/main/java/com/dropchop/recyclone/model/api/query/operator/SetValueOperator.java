package com.dropchop.recyclone.model.api.query.operator;

import com.dropchop.recyclone.model.api.query.ConditionOperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
public abstract class SetValueOperator<T> implements ConditionOperator {

  private Collection<T> values;

  public SetValueOperator() {
  }

  public SetValueOperator(Collection<T> values) {
    this.values = values;
  }

  protected Collection<T> getValues() {
    return values;
  }

  protected void setValues(Collection<T> values) {
    this.values = values;
  }

  protected void add(T value) {
    Collection<T> values = this.getValues();
    if (values == null) {
      values = new ArrayList<>();
      this.setValues(values);
    }
    values.add(value);
  }

  protected void add(Collection<T> valuesToAdd) {
    Collection<T> values = this.getValues();
    if (values == null) {
      values = new ArrayList<>();
      this.setValues(values);
    }
    values.addAll(valuesToAdd);
  }

  @SafeVarargs
  protected final void add(T... values) {
    this.add(Arrays.asList(values));
  }
}
