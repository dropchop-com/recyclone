package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class DateHistogram extends AggregationBucket {
  private String field;
  private String calendarInterval;
  private String timeZone;

  public DateHistogram(IQueryNode parent, String field, String calendarInterval, String timeZone) {
    super(parent, "date_histogram");
    setField(field);
    setCalendarInterval(calendarInterval);
    setTimeZone(timeZone);
  }

  public DateHistogram(IQueryNode parent, String field, String calendarInterval) {
    this(parent, field, calendarInterval, null);
  }

  public DateHistogram(String field, String calendarInterval, String timeZone) {
    this(null, field, calendarInterval, timeZone);
  }

  public DateHistogram(String field, String calendarInterval) {
    this(null, field, calendarInterval, null);
  }

  public DateHistogram() {
    this(null, null, null, null);
  }

  public void setField(String field) {
    this.field = field;
    if (field == null) {
      body.remove("field");
    } else {
      body.put("field", field);
    }
  }

  public void setCalendarInterval(String calendarInterval) {
    this.calendarInterval = calendarInterval;
    if (calendarInterval == null) {
      body.remove("calendar_interval");
    } else {
      body.put("calendar_interval", calendarInterval);
    }
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
    if (timeZone == null || timeZone.isBlank()) {
      body.remove("time_zone");
    } else {
      body.put("time_zone", timeZone);
    }
  }
}
