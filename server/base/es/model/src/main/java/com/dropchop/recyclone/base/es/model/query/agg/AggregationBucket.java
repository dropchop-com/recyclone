package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryObject;
import lombok.Getter;

/**
 * Base bucket aggregation with optional sub-aggregations.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
public abstract class AggregationBucket extends Aggregation {
  private IQueryObject aggs;

  protected AggregationBucket(IQueryNode parent, String type) {
    super(parent, type);
  }

  public void addAgg(String name, IQueryObject agg) {
    if (name == null || agg == null) {
      return;
    }
    if (aggs == null) {
      aggs = new QueryObject(this);
      this.put("aggs", aggs);
    }
    agg.setParent(this);
    aggs.put(name, agg);
  }
}
