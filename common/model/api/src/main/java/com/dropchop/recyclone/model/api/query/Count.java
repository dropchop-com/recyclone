package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Count extends BaseAggregation {

  public Count() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Count(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$count() {
    return super.getSubAggregations();
  }

  public void set$count(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public Count count(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Count count(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Count count(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
