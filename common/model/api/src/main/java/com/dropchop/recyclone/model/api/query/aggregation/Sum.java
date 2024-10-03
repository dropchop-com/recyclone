package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class Sum extends BaseAggregation {
  public Sum() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Sum(String name, String field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(name, field);
  }

  /*public List<Aggregation> get$sum() {
    return super.getSubAggregations();
  }

  public void set$sum(String name, String field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(name, field);
  }*/

  public Sum sum(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Sum sum(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Sum sum(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }

}
