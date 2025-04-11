package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class BoolQueryObject extends QueryNodeObject{
  IQueryNodeList must;
  IQueryNodeList should;
  IQueryNodeList mustNot;
  IQueryNodeList filter;
  private final int minimumShouldMatch;
  private int numClauses;

  public BoolQueryObject(IQueryNode parent, int minimumShouldMatch) {
    super(parent);
    if (parent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) parent).put("bool", this);
    }
    must = new QueryNodeList(this);
    mustNot = new QueryNodeList(this);
    filter = new QueryNodeList(this);
    should = new QueryNodeList(this);
    this.minimumShouldMatch = minimumShouldMatch;
  }

  public BoolQueryObject(IQueryNode parent) {
    this(parent, 1);
  }

  public BoolQueryObject() {
    this(null);
  }

  @Override
  public void setParent(IQueryNode parent) {
    IQueryNode prevParent = this.getParent();
    if (prevParent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) prevParent).remove("bool");
    }
    super.setParent(parent);
    if (parent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) parent).put("bool", this);
    }
  }

  private void add(IQueryNode node, IQueryNodeList list, String name) {
    if (node instanceof BoolQueryObject) {
      QueryNodeObject container = new QueryNodeObject(this);
      node.setParent(container);
      node = container;
    }

    list.add(node);
    if (list.size() == 1) {
      this.put(name, list.getFirst());
    } else {
      this.put(name, list);
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
      this.put("minimum_should_match", minimumShouldMatch);
    }
  }

  public void put() {
  }
}
