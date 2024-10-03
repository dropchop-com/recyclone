package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Avg extends BaseAggregation {

  public Avg() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Avg(String name, String field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(name, field);
  }

  /*public List<Aggregation> get$avg() {
    return super.getSubAggregations();
  }

  public void set$avg(String name, String field, List<Aggregation> subAggregations) {
    super.setAggregationField(name, field);
    super.setSubAggregations(subAggregations);
  }*/

  public Avg avg(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Avg avg(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Avg avg(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
