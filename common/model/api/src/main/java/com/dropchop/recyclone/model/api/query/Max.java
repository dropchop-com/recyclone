package com.dropchop.recyclone.model.api.query;

import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Max implements Aggregation {
  private String name;
  private String field;
  private List<Aggregation> subAggregations;

  public Max() {
    this.subAggregations = new ArrayList<>();
  }

  public Max(String name, String field, List<Aggregation> subAggregations) {
    this.name = name;
    this.field = field;
    this.subAggregations = subAggregations;
  }

  public List<Aggregation> get$max() {
    return subAggregations;
  }

  public void set$max(List<Aggregation> subAggregations) {
    this.subAggregations = subAggregations;
  }
}