package com.dropchop.recyclone.base.api.model.query.operator.text;

import com.dropchop.recyclone.base.api.model.query.Condition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdvancedTextParameters<T> implements Condition {
  private T name;
  private T value;

  // Optional parameters
  private Integer slop = 0;
  private Boolean inOrder = false;

  public AdvancedTextParameters() {

  }

  public AdvancedTextParameters(T name, T value) {
    this.name = name;
    this.value = value;
  }

  public AdvancedTextParameters(T name, T value, Boolean inOrder) {
    this.name = name;
    this.value = value;
    this.inOrder = inOrder;
  }

  public AdvancedTextParameters(T name, T value, Integer slop) {
    this.name = name;
    this.value = value;
    this.slop = slop;
  }

  public AdvancedTextParameters(T name, T value, Boolean inOrder, Integer slop) {
    this.name = name;
    this.value = value;
    this.inOrder = inOrder;
    this.slop = slop;
  }
}
