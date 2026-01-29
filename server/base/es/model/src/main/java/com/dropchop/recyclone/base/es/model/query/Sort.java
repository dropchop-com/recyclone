package com.dropchop.recyclone.base.es.model.query;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 28. 01. 2026.
 */
public class Sort extends QueryObject {
  private final IQueryList fields = new QueryList();

  public Sort(IQueryNode parent) {
    super(parent);
    this.put("sort", fields);
  }

  public void addSort(String field, String order, String numericType) {
    SortField sortNode = new SortField(this, field, order, numericType);
    this.fields.add(sortNode);
  }

  public void addSort(String field, String order) {
    SortField sortNode = new SortField(this, field, order);
    this.fields.add(sortNode);
  }
}
