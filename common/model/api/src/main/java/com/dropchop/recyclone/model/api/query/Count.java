package com.dropchop.recyclone.model.api.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Count implements Aggregation {

  private String name;
  private String field;
  private List<Aggregation> subAggregations;

  public Count(String name, String field, Aggregation... subAggregations) {
    this.name = name;
    this.field = field;
    this.subAggregations = List.of(subAggregations);
  }

  public List<Aggregation> get$count() {
    return subAggregations;
  }

  public void set$count(List<Aggregation> subAggregations) {
    this.subAggregations = subAggregations;
  }
}
