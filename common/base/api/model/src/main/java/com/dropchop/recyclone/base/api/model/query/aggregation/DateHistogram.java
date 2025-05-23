package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@SuppressWarnings("unused")
public class DateHistogram extends BucketAggregation {
  private String calendar_interval;

  public DateHistogram(String name, String field, String calendar_interval, Aggregation... aggs) {
    this(name, field, calendar_interval, new AggregationList(Arrays.asList(aggs)));
  }

  public DateHistogram(String name, String field, String calendar_interval, AggregationList aggs) {
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
