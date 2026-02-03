package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.QueryObject;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class MatchAll extends QueryObject {
  private final Float boost;
  private final QueryObject body = new QueryObject();

  public MatchAll(IQueryNode parent, Float boost) {
    super(parent);
    this.boost = boost;
    if (boost != null) {
      this.body.put("boost", boost);
    }
    this.put("match_all", this.body);
  }

  public MatchAll(IQueryNode parent) {
    this(parent, null);
  }

  public MatchAll() {
    this(null);
  }
}
