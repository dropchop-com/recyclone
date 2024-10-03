package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistogramAggregation extends BaseAggregation implements Aggregation {
  private String calenderInterval;

  protected void setAggregationField(String name, String field, String calenderInterval) {
    super.setAggregationField(name, field);
    this.calenderInterval = calenderInterval;
  }
}
