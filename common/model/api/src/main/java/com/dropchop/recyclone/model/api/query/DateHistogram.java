package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class DateHistogram extends BaseAggregation {

  public DateHistogram() {
    this.setSubAggregations(new ArrayList<>());
  }

  public DateHistogram(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
  }

  public List<Aggregation> get$dateHistogram() {
    return super.getSubAggregations();
  }

  public void set$dateHistogram(AggregationField field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(field);
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
