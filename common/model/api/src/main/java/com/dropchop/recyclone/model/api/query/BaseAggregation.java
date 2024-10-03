package com.dropchop.recyclone.model.api.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class BaseAggregation implements Aggregation {
  private List<Aggregation> subAggregations;

  protected List<Aggregation> getSubAggregations() {
    return subAggregations;
  }

  protected void setSubAggregations(List<Aggregation> subAggregations) {
    this.subAggregations = subAggregations;
  }

  protected void add(Aggregation subAggregation) {
    List<Aggregation> subAggregations = this.getSubAggregations();
    if (subAggregations == null) {
      subAggregations = new ArrayList<>();
      this.setSubAggregations(subAggregations);
    }
    subAggregations.add(subAggregation);
  }

  protected void add(Collection<Aggregation> subAggregationsToAdd) {
    List<Aggregation> subAggregations1 = this.getSubAggregations();
    if (subAggregations1 == null) {
      subAggregations1 = new ArrayList<>();
      this.setSubAggregations(subAggregations1);
    }
    subAggregations1.addAll(subAggregationsToAdd);
  }

  protected void add(Aggregation ... subAggregationsToAdd) {
    this.add(Arrays.asList(subAggregationsToAdd));
  }
}