package com.dropchop.recyclone.model.api.query;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class Sum extends AggregationImpl {
  public Sum() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Sum(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$sum() {
    return super.getSubAggregations();
  }

  public void set$sum(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public Sum sum(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Sum sum(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Sum sum(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }

}
