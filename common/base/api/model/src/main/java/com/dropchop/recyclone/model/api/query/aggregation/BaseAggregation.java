package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Iterator;

@Getter
@Setter
public abstract class BaseAggregation implements Aggregation {
  private AggregationList aggs;
  private String name;
  private String field;

  public BaseAggregation(String name, String field, Aggregation ... aggs) {
    this(name, field, new AggregationList(Arrays.asList(aggs)));
  }

  public BaseAggregation(String name, String field, AggregationList aggs) {
    this.name = name;
    this.field = field;
    this.aggs = aggs;
  }

  public BaseAggregation(String name, String field) {
    this.name = name;
    this.field = field;
  }

  public BaseAggregation() {
  }

  public Iterator<Aggregation> iterator() {
    return this.aggs.iterator();
  }
}