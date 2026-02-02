package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

public class Sum extends AggregationField {
  public Sum(IQueryNode parent, String field) {
    super(parent, "sum", field);
  }

  public Sum(String field) {
    this(null, field);
  }

  public Sum() {
    this(null, null);
  }
}
