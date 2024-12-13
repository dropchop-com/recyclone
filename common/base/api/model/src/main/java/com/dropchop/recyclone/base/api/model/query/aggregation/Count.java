package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;

@SuppressWarnings("unused")
public class Count extends BaseAggregation {

  public Count(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Count(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Count(String name, String field) {
    super(name, field);
  }

  public Count() {
  }
}
