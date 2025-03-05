package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseAggregation implements Aggregation {
  private String name;
  private String field;

  public BaseAggregation(String name, String field) {
    this.name = name;
    this.field = field;
  }

  public BaseAggregation() {
  }

}