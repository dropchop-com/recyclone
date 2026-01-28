package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class SpanNearObject extends QueryNodeObject {
  private final IQueryNodeList clauses;
  private final Integer slop;
  private final Boolean inOrder;
  private final QueryNodeObject self = new QueryNodeObject();

  public SpanNearObject(IQueryNode parent, Integer slop, Boolean inOrder) {
    super(parent);
    this.clauses = new QueryNodeList(this);
    this.inOrder = inOrder;
    this.slop = slop;

    this.self.put("clauses", clauses);
    this.self.put("in_order", inOrder);
    this.self.put("slop", slop);
    this.put("span_near", this.self);
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
}
