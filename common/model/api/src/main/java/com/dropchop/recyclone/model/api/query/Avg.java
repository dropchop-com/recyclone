package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Avg extends AggregationImpl {

  public Avg() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Avg(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
  }

  public List<Aggregation> get$avg() {
    return super.getSubAggregations();
  }

  public void set$avg(AggregationField field, List<Aggregation> subAggregations) {
    super.setAggregationField(field);
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
