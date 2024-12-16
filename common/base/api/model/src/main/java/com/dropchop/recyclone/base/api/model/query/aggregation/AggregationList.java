package com.dropchop.recyclone.base.api.model.query.aggregation;

import com.dropchop.recyclone.base.api.model.query.Aggregation;

import java.util.ArrayList;
import java.util.Collection;

public class AggregationList extends ArrayList<Aggregation> {
  public AggregationList(int initialCapacity) {
    super(initialCapacity);
  }

  public AggregationList() {
  }

  public AggregationList(Collection<? extends Aggregation> c) {
    super(c);
  }
}
