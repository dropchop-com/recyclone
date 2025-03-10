package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
@Getter
@Setter
public class Terms extends BucketAggregation {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer size = null;

  public Terms(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Terms(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Terms(String name, String field, Integer size, Aggregation... aggs) {
    super(name, field, aggs);
    this.size = size;
  }

  public Terms(String name, String field, Integer size, AggregationList aggs) {
    super(name, field, aggs);
    this.size = size;
  }

  public Terms(String name, String field) {
    super(name, field);
  }

  public Terms() {
  }
}
