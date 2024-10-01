package com.dropchop.recyclone.model.api.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Cardinality implements Aggregation {

  private String name;
  private String field;
  private List<Aggregation> subAggregations;

  public Cardinality(String name, String field, Aggregation... subAggregations) {
    this.name = name;
    this.field = field;
    this.subAggregations = List.of(subAggregations);
  }

  public List<Aggregation> get$cardinality() {
    return subAggregations;
  }

  public void set$cardinality(List<Aggregation> subAggregations) {
    this.subAggregations = subAggregations;
  }
}
