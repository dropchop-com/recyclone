package com.dropchop.recyclone.model.api.query;

import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Getter
@SuppressWarnings("unused")
public class Min extends BaseAggregation {
  
  public Min() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Min(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$min() {
    return super.getSubAggregations();
  }

  public void set$min(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public Min min(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Min min(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Min min(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
