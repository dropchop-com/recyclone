package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.List;

@SuppressWarnings("unused")
public class Count extends BaseAggregation {

  public Count(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Count(String name, String field, List<AggregationWrapper> aggs) {
    super(name, field, aggs);
  }

  public Count(String name, String field) {
    super(name, field);
  }

  public Count() {
  }
}
