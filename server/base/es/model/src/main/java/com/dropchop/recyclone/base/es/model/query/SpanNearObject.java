package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class SpanNearObject extends QueryNodeObject {
  private final IQueryNodeList clauses;
  private final Integer slop;
  private final Boolean inOrder;

  public SpanNearObject(IQueryNode parent, Integer slop, Boolean inOrder) {
    super(parent);
    this.clauses = new QueryNodeList(this);
    this.inOrder = inOrder;
    this.slop = slop;

    this.put("clauses", clauses);
    this.put("in_order", inOrder);
    this.put("slop", slop);
  }

  public SpanNearObject addClause(IQueryNode clause) {
    if (clause instanceof BoolQueryObject) {
      QueryNodeObject container = new QueryNodeObject(this);
      clause.setParent(container);
      clauses.add(container);
    } else {
      clauses.add(clause);
    }
    return this;
  }

  @Override
  public void setParent(IQueryNode parent) {
    IQueryNode prevParent = this.getParent();
    if (prevParent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) prevParent).remove("span_near");
    }
    super.setParent(parent);
    if (parent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) parent).put("span_near", this);
    }
  }
}
