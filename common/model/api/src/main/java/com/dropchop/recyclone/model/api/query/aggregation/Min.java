package com.dropchop.recyclone.model.api.query.aggregation;

import com.dropchop.recyclone.model.api.query.Aggregation;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Getter
@SuppressWarnings("unused")
public class Min extends BaseAggregation {
  
  public Min() {
    this.setSubAggregations(new ArrayList<>());
  }

  public Min(String name, String field, List<Aggregation> subAggregations) {
    super.setSubAggregations(subAggregations);
    super.setAggregationField(name, field);
  }

  public List<Aggregation> get$min() {
    return super.getSubAggregations();
  }

  public void set$min(String name, String field, List<Aggregation> subAggregations) {
    super.setAggregationField(name, field);
    super.setSubAggregations(subAggregations);
  }

  public Min min(Aggregation subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Min min(Collection<Aggregation> subAggregationsToAdd) {
    super.add(subAggregationsToAdd);
    return this;
  }

  public Min min(Aggregation ... subAggregationsToAdd) {
    this.add(subAggregationsToAdd);
    return this;
  }
}
