package com.dropchop.recyclone.base.api.model.query.aggregation;

@SuppressWarnings("unused")
public class Max extends BaseAggregation {

  //Removed because of unsupported sub-aggregations
  /*public Max(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Max(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }*/

  public Max(String name, String field) {
    super(name, field);
  }

  public Max() {
  }
}