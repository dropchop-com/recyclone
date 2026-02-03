package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@SuppressWarnings("unused")
public class DateHistogram extends BucketAggregation {
  private String calendarInterval;
  private String timeZone;

  public DateHistogram(String name, String field, String calendarInterval, String timeZone, Aggregation... aggs) {
    this(name, field, calendarInterval, timeZone, new AggregationList(Arrays.asList(aggs)));
  }

  public DateHistogram(String name, String field, String calendarInterval, String timeZone, AggregationList aggs) {
    super(name, field, aggs);
    this.calendarInterval = calendarInterval;
    this.timeZone = timeZone;
  }

  public DateHistogram(String name, String field, String calendarInterval, String timeZone) {
    super(name, field);
    this.calendarInterval = calendarInterval;
    this.timeZone = timeZone;
  }

  public DateHistogram(String name, String field, String calendarInterval, Aggregation... aggs) {
    this(name, field, calendarInterval, new AggregationList(Arrays.asList(aggs)));
  }

  public DateHistogram(String name, String field, String calendarInterval, AggregationList aggs) {
    super(name, field, aggs);
    this.calendarInterval = calendarInterval;
  }

  public DateHistogram(String name, String field, String calendarInterval) {
    super(name, field);
    this.calendarInterval = calendarInterval;
  }

  public DateHistogram() {
  }

}
