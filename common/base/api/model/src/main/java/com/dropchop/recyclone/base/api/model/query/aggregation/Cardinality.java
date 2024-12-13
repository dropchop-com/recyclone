package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;

@SuppressWarnings("unused")
public class Cardinality extends BaseAggregation {

  public Cardinality(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Cardinality(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Cardinality(String name, String field) {
    super(name, field);
  }

  public Cardinality() {
  }
}
