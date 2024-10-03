package com.dropchop.recyclone.model.api.query;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Sum extends BaseAggregation {
  public Sum() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Sum(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
  }

  public List<Aggregation> get$sum() {
    return super.getSubAggregations();
  }

  public void set$sum(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
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
