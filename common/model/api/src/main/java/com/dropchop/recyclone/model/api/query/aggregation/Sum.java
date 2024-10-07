package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.List;

@SuppressWarnings("unused")
public class Sum extends BaseAggregation {
  
  public Sum(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Sum(String name, String field, List<AggregationWrapper> aggs) {
    super(name, field, aggs);
  }

  public Sum(String name, String field) {
    super(name, field);
  }

  public Sum() {
  }

}
