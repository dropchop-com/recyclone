package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class Min extends BaseAggregation {

  public Min(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Min(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Min(String name, String field) {
    super(name, field);
  }

  public Min() {
  }
}
