package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

public class Count extends AggregationField {
  public Count(IQueryNode parent, String field) {
    super(parent, "value_count", field);
  }

  public Count(String field) {
    this(null, field);
  }

  public Count() {
    this(null, null);
  }
}
