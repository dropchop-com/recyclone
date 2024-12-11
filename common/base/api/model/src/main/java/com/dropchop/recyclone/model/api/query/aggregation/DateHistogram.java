package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@SuppressWarnings("unused")
public class DateHistogram extends BaseAggregation implements Aggregation {
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
