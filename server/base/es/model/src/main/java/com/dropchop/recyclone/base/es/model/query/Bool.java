package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@SuppressWarnings("unused")
public class Bool extends QueryObject {
  private final IQueryList must = new QueryList(this);
  private final IQueryList should = new QueryList(this);
  private final IQueryList mustNot = new QueryList(this);
  private final IQueryList filter = new QueryList(this);
  private final IQueryObject self = new QueryObject();

  @Setter
  private int minimumShouldMatch;
  @Setter
  private int numClauses;

  public Bool(IQueryNode parent, int minimumShouldMatch) {
    super(parent);
    this.minimumShouldMatch = minimumShouldMatch;
    this.put("bool", this.self);
  }

  public Bool(IQueryNode parent) {
    this(parent, 1);
  }

  public Bool() {
    this(null);
  }

  private void add(IQueryNode node, IQueryList list, String name) {
    list.add(node);
    if (list.size() == 1) {
      this.self.put(name, list.getFirst());
    } else {
      this.self.put(name, list);
    }
    numClauses++;
  }

  public void must(IQueryNode node) {
    this.add(node, must, "must");
  }

  public void mustNot(IQueryNode node) {
    this.add(node, mustNot, "must_not");
  }

  public void filter(IQueryNode node) {
    this.add(node, filter, "filter");
  }

  public void should(IQueryNode node) {
    this.add(node, should, "should");
    if (minimumShouldMatch > -1) {
      this.self.put("minimum_should_match", minimumShouldMatch);
    }
  }
}
