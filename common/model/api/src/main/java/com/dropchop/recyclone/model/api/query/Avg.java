package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Avg extends BaseAggregation {

  public Avg() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Avg(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$avg() {
    return super.getSubAggregations();
  }

  public void set$avg(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public Avg avg(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Avg avg(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Avg avg(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
