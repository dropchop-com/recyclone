package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class BaseAggregation implements Aggregation {
  private AggregationWrappers aggs = new AggregationWrappers();
  private String name;
  private String field;

  public BaseAggregation(String name, String field, Aggregation ... aggs) {
    this(name, field, Arrays.stream(aggs).map(AggregationWrapper::new).collect(Collectors.toCollection(AggregationWrappers::new)));
  }

  public BaseAggregation(String name, String field, List<AggregationWrapper> aggs) {
    this.name = name;
    this.field = field;
    this.aggs = aggs.stream().collect(Collectors.toCollection(AggregationWrappers::new));
  }

  public BaseAggregation(String name, String field) {
    this.name = name;
    this.field = field;
  }

  public BaseAggregation() {
  }
}