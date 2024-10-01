package com.dropchop.recyclone.model.api.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DateHistogram implements Aggregation {

  private String name;
  private String field;
  private List<Aggregation> subAggregations;

  public DateHistogram(String name, String field, Aggregation... subAggregations) {
    this.name = name;
    this.field = field;
    this.subAggregations = List.of(subAggregations);
  }

  public List<Aggregation> get$dateHistogram() {
    return subAggregations;
  }

  public void set$dateHistogram(List<Aggregation> subAggregations) {
    this.subAggregations = subAggregations;
  }
}
