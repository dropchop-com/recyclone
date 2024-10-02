package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DateHistogram extends AggregationImpl {

  public DateHistogram() {
    this.setSubAggregations(new ArrayList<>());
  }

  public DateHistogram(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public List<Aggregation> get$dateHistogram() {
    return super.getSubAggregations();
  }

  public void set$dateHistogram(List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
  }

  public DateHistogram dateHistogram(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public DateHistogram dateHistogram(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public DateHistogram dateHistogram(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
