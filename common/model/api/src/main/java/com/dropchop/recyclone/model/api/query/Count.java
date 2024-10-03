package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Count extends AggregationImpl {

  public Count() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Count(AggregationField field, List<Aggregation> subAggregations) {
    super.setAggregationField(field);
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$count() {
    return super.getSubAggregations();
  }

  public void set$count(AggregationField field, List<Aggregation> subAggregations) {
    super.setAggregationField(field);
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
