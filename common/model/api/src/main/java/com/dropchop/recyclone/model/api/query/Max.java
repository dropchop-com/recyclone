package com.dropchop.recyclone.model.api.query;

import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class Max extends AggregationImpl {

  public Max() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Max(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
  }

  public List<Aggregation> get$max() {
    return super.getSubAggregations();
  }

  public void set$max(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
  }

  public Max max(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Max max(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Max max(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}