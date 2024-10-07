package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;

import java.util.List;

@Getter
@SuppressWarnings("unused")
public class Min extends BaseAggregation {

  public Min(String name, String field, Aggregation... aggs) {
    super(name, field, aggs);
  }

  public Min(String name, String field, AggregationList aggs) {
    super(name, field, aggs);
  }

  public Min(String name, String field) {
    super(name, field);
  }

  public Min() {
  }
}
