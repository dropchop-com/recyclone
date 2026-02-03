package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
public class Stats extends AggregationField {
  public Stats(IQueryNode parent, String field) {
    super(parent, "stats", field);
  }
}
