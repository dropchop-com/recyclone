package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryObject;
import lombok.Getter;

/**
 * Base elastic aggregation node.
 */
@Getter
public abstract class Aggregation extends QueryObject {
  private final String type;
  protected final IQueryObject body;

  protected Aggregation(IQueryNode parent, String type) {
    super(parent);
    this.type = type;
    this.body = new QueryObject(this);
    this.put(type, body);
  }
}
