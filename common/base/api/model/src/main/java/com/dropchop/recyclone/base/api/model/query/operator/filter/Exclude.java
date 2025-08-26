package com.dropchop.recyclone.base.api.model.query.operator.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Exclude {
  private List<String> value;

  public Exclude(List<String> value) {
    this.value = value;
  }

  public Exclude(String regEx) {
    this.value = List.of(regEx);
  }
}
