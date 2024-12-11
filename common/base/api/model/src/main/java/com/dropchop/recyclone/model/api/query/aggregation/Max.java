package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.List;

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