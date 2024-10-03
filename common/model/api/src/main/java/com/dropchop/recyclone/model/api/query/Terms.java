package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Terms extends BaseAggregation {
  public Terms() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Terms(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$terms() {
    return super.getSubAggregations();
  }

  public void set$terms(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public Terms terms(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Terms terms(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Terms terms(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
