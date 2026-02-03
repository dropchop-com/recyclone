package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
public class Cardinality extends AggregationField {
  public Cardinality(IQueryNode parent, String field) {
    super(parent, "cardinality", field);
  }
}
