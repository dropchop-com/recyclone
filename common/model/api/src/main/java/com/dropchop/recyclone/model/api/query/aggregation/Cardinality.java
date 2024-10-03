package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class Cardinality extends BaseAggregation {

  public Cardinality() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Cardinality(String name, String field, List<Aggregation> subAggregations) {
    super.setAggregationField(name, field);
    super.setSubAggregations(subAggregations);
  }

  /*public List<Aggregation> get$cardinality() {
    return super.getSubAggregations();
  }

  public void set$cardinality(String name, String field, List<Aggregation> subAggregations) {
    super.setAggregationField(name, field);
    super.setSubAggregations(subAggregations);
  }*/

  public Cardinality cardinality(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Cardinality cardinality(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Cardinality cardinality(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
