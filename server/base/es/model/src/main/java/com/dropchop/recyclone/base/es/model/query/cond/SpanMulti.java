package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 01. 2026.
 */
public class SpanMulti extends QueryObject {

  @SuppressWarnings("FieldCanBeLocal")
  private final IQueryObject body = new QueryObject(this);

  public SpanMulti(IQueryNode parent, IQueryNode child) {
    super(parent);
    child.setParent(this);
    body.put("match", child);
    this.put("span_multi", body);
  }
}
