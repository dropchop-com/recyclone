package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

public class Stats extends AggregationField {
  public Stats(IQueryNode parent, String field) {
    super(parent, "stats", field);
  }

  public Stats(String field) {
    this(null, field);
  }
}
