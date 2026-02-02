package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

public class Cardinality extends AggregationField {
  public Cardinality(IQueryNode parent, String field) {
    super(parent, "cardinality", field);
  }

  public Cardinality(String field) {
    this(null, field);
  }
}
