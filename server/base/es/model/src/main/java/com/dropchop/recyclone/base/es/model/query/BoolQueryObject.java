package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@SuppressWarnings("unused")
public class BoolQueryObject extends QueryNodeObject {
  private final IQueryNodeList must = new QueryNodeList(this);
  private final IQueryNodeList should = new QueryNodeList(this);
  private final IQueryNodeList mustNot = new QueryNodeList(this);
  private final IQueryNodeList filter = new QueryNodeList(this);
  private final QueryNodeObject self = new QueryNodeObject();

  @Setter
  private int minimumShouldMatch;
  @Setter
  private int numClauses;

  public BoolQueryObject(IQueryNode parent, int minimumShouldMatch) {
    super(parent);
    this.minimumShouldMatch = minimumShouldMatch;
    this.put("bool", this.self);
  }

  public BoolQueryObject(IQueryNode parent) {
    this(parent, 1);
  }

  public BoolQueryObject() {
    this(null);
  }

  private void add(IQueryNode node, IQueryNodeList list, String name) {
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
