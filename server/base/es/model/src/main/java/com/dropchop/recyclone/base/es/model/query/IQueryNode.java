package com.dropchop.recyclone.base.es.model.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 01. 20.
 */
@SuppressWarnings("unused")
public interface IQueryNode {
  IQueryNode getParent();
  void setParent(IQueryNode parent);
}

class QueryNode implements IQueryNode {

  private IQueryNode parent;

  public QueryNode() {
    this.parent = null;
  }

  public QueryNode(IQueryNode parent) {
    this.parent = parent;
  }

  @Override
  public IQueryNode getParent() {
    return parent;
  }

  @Override
  public void setParent(IQueryNode parent) {
    this.parent = parent;
  }
}
