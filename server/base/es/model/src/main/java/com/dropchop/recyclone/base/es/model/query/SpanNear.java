package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class SpanNear extends QueryObject {
  private final IQueryList clauses;
  private final Integer slop;
  private final Boolean inOrder;
  private final IQueryObject self = new QueryObject();

  public SpanNear(IQueryNode parent, Integer slop, Boolean inOrder) {
    super(parent);
    this.clauses = new QueryList(this);
    this.inOrder = inOrder;
    this.slop = slop;

    this.self.put("clauses", clauses);
    this.self.put("in_order", inOrder);
    this.self.put("slop", slop);
    this.put("span_near", this.self);
  }

  public SpanNear addClause(IQueryNode clause) {
    if (clause instanceof Bool) {
      QueryObject container = new QueryObject(this);
      clause.setParent(container);
      clauses.add(container);
    } else {
      clauses.add(clause);
    }
    return this;
  }
}
