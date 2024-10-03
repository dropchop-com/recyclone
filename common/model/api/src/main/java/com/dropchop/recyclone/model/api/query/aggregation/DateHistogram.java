package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class DateHistogram extends HistogramAggregation {

  public DateHistogram() {
    this.setSubAggregations(new ArrayList<>());
  }

  public DateHistogram(String name, String field, String calender_interval, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(name, field);
    super.setCalenderInterval(calender_interval);
  }

  /*public List<Aggregation> get$dateHistogram() {
    return super.getSubAggregations();
  }

  public void set$dateHistogram(String name, String field, String calender_interval, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(name, field);
    super.setCalenderInterval(calender_interval);
  }*/

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
