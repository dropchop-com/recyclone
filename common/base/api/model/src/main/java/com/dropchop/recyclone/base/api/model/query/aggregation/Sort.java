package com.dropchop.recyclone.base.api.model.query.aggregation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sort {
  private String field;
  private String value;

  public Sort(String field, String value) {
    this.field = field;
    this.value = value;
  }
}
