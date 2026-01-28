package com.dropchop.recyclone.base.es.model.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 28. 01. 2026.
 */
public class SortNodeObject extends QueryNodeObject {
  private final IQueryNodeList fields = new QueryNodeList();

  public SortNodeObject(IQueryNode parent) {
    super(parent);
    this.put("sort", fields);
  }

  public void addSort(String field, String order, String numericType) {
    SortFieldNodeObject sortNode = new SortFieldNodeObject(this, field, order, numericType);
    this.fields.add(sortNode);
  }

  public void addSort(String field, String order) {
    SortFieldNodeObject sortNode = new SortFieldNodeObject(this, field, order);
    this.fields.add(sortNode);
  }
}
