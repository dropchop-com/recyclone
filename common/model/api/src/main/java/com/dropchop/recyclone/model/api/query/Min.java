package com.dropchop.recyclone.model.api.query;

import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Getter
public class Min extends AggregationImpl {
  
  public Min() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Min(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
  }

  public List<Aggregation> get$min() {
    return super.getSubAggregations();
  }

  public void set$min(AggregationField field, List<Aggregation> subAggregations) {
    super.setAggregationField(field);
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
