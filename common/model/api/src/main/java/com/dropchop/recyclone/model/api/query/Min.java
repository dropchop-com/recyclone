package com.dropchop.recyclone.model.api.query;

import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Min implements Aggregation {
  private String name;
  private String field;
  private List<Aggregation> subAggregations;

  public Min(String name, String field, Aggregation... subAggregations) {
    this.name = name;
    this.field = field;
    this.subAggregations = new ArrayList<>(List.of(subAggregations));
  }

  public List<Aggregation> get$min() {
    return subAggregations;
  }

  public void set$min(List<Aggregation> subAggregations) {
    this.subAggregations = subAggregations;
  }
}
