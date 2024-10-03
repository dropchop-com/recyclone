package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class DateHistogram extends BaseAggregation {
  private String calendar_interval;

  public DateHistogram(String name, String field, String calendar_interval, Aggregation... aggs) {
    this(name, field, calendar_interval, Arrays.stream(aggs).map(AggregationContainer::new).toList());
  }

  public DateHistogram(String name, String field, String calendar_interval, List<AggregationContainer> aggs) {
    super(name, field, aggs);
    this.calendar_interval = calendar_interval;
  }

  public DateHistogram(String name, String field, String calendar_interval) {
    super(name, field);
    this.calendar_interval = calendar_interval;
  }

  public DateHistogram() {
  }

}
