package com.dropchop.recyclone.model.api.query;

import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

@Getter
public class Sum implements Aggregation {
  private String name;
  private String field;
  private List<Aggregation> subAggregations;

  public Sum(String name, String field, Aggregation... subAggregations) {
    this.name = name;
    this.field = field;
    this.subAggregations = new ArrayList<>(List.of(subAggregations));
  }

}
