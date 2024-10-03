package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Cardinality extends BaseAggregation {

  public Cardinality() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Cardinality(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$cardinality() {
    return super.getSubAggregations();
  }

  public void set$cardinality(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public Cardinality cardinality(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Cardinality cardinality(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Cardinality cardinality(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
