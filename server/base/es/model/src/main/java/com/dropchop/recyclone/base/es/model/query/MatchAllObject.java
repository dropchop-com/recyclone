package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class MatchAllObject extends QueryNodeObject {
  private final Float boost;
  private final QueryNodeObject self = new QueryNodeObject();

  public MatchAllObject(IQueryNode parent, Float boost) {
    super(parent);
    this.boost = boost;
    if (boost != null) {
      this.self.put("boost", boost);
    }
    this.put("match_all", this.self);
  }

  public MatchAllObject(IQueryNode parent) {
    this(parent, null);
  }

  public MatchAllObject() {
    this(null);
  }
}
