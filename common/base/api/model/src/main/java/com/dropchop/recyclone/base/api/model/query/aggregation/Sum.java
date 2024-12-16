package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;

@SuppressWarnings("unused")
public class Sum extends BaseAggregation {
  
  public Sum(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Sum(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Sum(String name, String field) {
    super(name, field);
  }

  public Sum() {
  }

}
