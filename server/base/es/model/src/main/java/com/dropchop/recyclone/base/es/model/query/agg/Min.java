package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

public class Min extends AggregationField {
  public Min(IQueryNode parent, String field) {
    super(parent, "min", field);
  }

  public Min(String field) {
    this(null, field);
  }
}
