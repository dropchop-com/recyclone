package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class MatchAllObject extends QueryNodeObject {
  private final Float boost;

  public MatchAllObject(IQueryNode parent, Float boost) {
    super(parent);
    this.boost = boost;
    if (boost != null) {
      this.put("boost", boost);
    }
  }

  public MatchAllObject(IQueryNode parent) {
    this(parent, null);
  }

  public MatchAllObject() {
    this(null, null);
  }

  @Override
  public void setParent(IQueryNode parent) {
    IQueryNode prevParent = this.getParent();
    if (prevParent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) prevParent).remove("match_all");
    }
    super.setParent(parent);
    if (parent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) parent).put("match_all", this);
    }
  }
}
