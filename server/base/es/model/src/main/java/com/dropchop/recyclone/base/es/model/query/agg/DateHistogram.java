package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
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
