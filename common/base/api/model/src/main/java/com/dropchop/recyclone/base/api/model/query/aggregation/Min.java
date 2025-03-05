package com.dropchop.recyclone.base.api.model.query.aggregation;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class Min extends BaseAggregation {

  public Min(String name, String field) {
    super(name, field);
  }

  public Min() {
  }
}
