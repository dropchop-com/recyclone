package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.List;

public class AggregationImpl implements Aggregation {
  private String name;
  private String field;
  private List<Aggregation> subAggregations;

  public AggregationImpl() {
  }

  public AggregationImpl(String name, String field) {
    this.name = name;
    this.field = field;
    this.subAggregations = new ArrayList<>();
  }

  public AggregationImpl addSubAggregation(List<Aggregation> subAggregation) {
    this.subAggregations.add((Aggregation) subAggregation);
    return this;
  }
}