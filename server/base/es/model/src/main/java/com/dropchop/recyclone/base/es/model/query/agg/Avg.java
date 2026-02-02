package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

public class Avg extends AggregationField {
  public Avg(IQueryNode parent, String field) {
    super(parent, "avg", field);
  }

  public Avg(String field) {
    this(null, field);
  }

  public Avg() {
    this(null, null);
  }
}
