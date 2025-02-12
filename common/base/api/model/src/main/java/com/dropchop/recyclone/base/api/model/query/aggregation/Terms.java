package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;

@SuppressWarnings("unused")
public class Terms extends BaseAggregation {
  public Terms(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Terms(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Terms(String name, String field) {
    super(name, field);
  }

  public Terms() {
  }
}
