package com.dropchop.recyclone.base.es.repo.mapper.parser;

import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;
import com.dropchop.recyclone.base.api.repo.mapper.SpanNearObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SpanNearBuilder {
  private final List<QueryNodeObject> clauses = new ArrayList<>();
  private Boolean inOrder;
  private Integer slop;

  public SpanNearBuilder withInOrder(Boolean inOrder) {
    this.inOrder = inOrder;
    return this;
  }

  public SpanNearBuilder withSlop(Integer slop) {
    this.slop = slop;
    return this;
  }

  public SpanNearBuilder addClause(QueryNodeObject clause) {
    clauses.add(clause);
    return this;
  }

  public QueryNodeObject build() {
    QueryNodeObject spanNearObject = new QueryNodeObject();
    SpanNearObject spanNear = new SpanNearObject(null, inOrder, slop);

    clauses.forEach(clause -> {
      if (clause != null) {
        clause.setParent(spanNear);
      }
      spanNear.getClauses().add(clause);
    });

    spanNearObject.put("span_near", spanNear);
    return spanNearObject;
  }
}
