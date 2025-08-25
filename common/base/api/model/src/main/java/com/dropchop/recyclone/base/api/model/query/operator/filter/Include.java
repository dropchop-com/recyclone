package com.dropchop.recyclone.base.api.model.query.operator.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Include {
  private List<String> value;

  public Include(List<String> value) {
    this.value = value;
  }

  public Include(String regEx) {
    this.value = List.of(regEx);
  }
}
