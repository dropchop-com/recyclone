package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

@Getter
@Setter
public abstract class BaseAggregation implements Aggregation {
  private AggregationList aggs = new AggregationList();
  private String name;
  private String field;

  public BaseAggregation(String name, String field, Aggregation... aggs) {
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
    return aggs != null ? aggs.iterator() : Collections.emptyIterator();
  }
}