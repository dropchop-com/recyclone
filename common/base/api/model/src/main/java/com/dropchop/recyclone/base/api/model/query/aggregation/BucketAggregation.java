package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

@Getter
@Setter
public class BucketAggregation extends BaseAggregation {
  private AggregationList aggs = new AggregationList();

  public BucketAggregation(String name, String field, Aggregation... aggs) {
    this(name, field, new AggregationList(Arrays.asList(aggs)));
  }

  public BucketAggregation(String name, String field, AggregationList aggs) {
    super(name, field);
    this.aggs = aggs;
  }

  public BucketAggregation(String name, String field) {
    super(name, field);
  }

  public BucketAggregation() {
  }

  public Iterator<Aggregation> iterator() {
    return aggs != null ? aggs.iterator() : Collections.emptyIterator();
  }
}
