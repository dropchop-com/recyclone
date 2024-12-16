package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;

@SuppressWarnings("unused")
public class Max extends BaseAggregation {

  public Max(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Max(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Max(String name, String field) {
    super(name, field);
  }

  public Max() {
  }
}