package com.dropchop.recyclone.model.api.query;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class AggregationField extends HashMap<String, String> implements Aggregation {
  private String name;
  private String field;
  private String calenderInterval;

  public AggregationField(String name, String field) {
    this.put("name", name);
    this.put("field", field);
    this.name = name;
    this.field = field;
  }

  public AggregationField(String name, String field, String calenderInterval) {
    this.put("name", name);
    this.put("field", field);
    this.put("calender_interval", calenderInterval);
    this.name = name;
    this.field = field;
    this.calenderInterval = calenderInterval;
  }
}
