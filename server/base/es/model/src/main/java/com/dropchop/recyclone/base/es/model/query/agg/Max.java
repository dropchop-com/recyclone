package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

public class Max extends AggregationField {
  public Max(IQueryNode parent, String field) {
    super(parent, "max", field);
  }

  public Max(String field) {
    this(null, field);
  }

  public Max() {
    this(null, null);
  }
}
