package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class MatchAll extends QueryObject {
  private final Float boost;
  private final QueryObject self = new QueryObject();

  public MatchAll(IQueryNode parent, Float boost) {
    super(parent);
    this.boost = boost;
    if (boost != null) {
      this.self.put("boost", boost);
    }
    this.put("match_all", this.self);
  }

  public MatchAll(IQueryNode parent) {
    this(parent, null);
  }

  public MatchAll() {
    this(null);
  }
}
