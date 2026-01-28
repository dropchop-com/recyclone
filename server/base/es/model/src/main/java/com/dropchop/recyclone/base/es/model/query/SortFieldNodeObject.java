package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class SortFieldNodeObject extends QueryNodeObject {
  private String field;
  private String order;
  private String mode;
  private String missing;
  private String numericType;

  public SortFieldNodeObject(IQueryNode parent, String field, String order, String numericType) {
    super(parent);
    this.field = field;
    this.order = order;
    this.numericType = numericType;
    this.put(field, createSortConfig());
  }

  public SortFieldNodeObject(IQueryNode parent, String field, String order) {
    this(parent, field, order, null);
  }

  public SortFieldNodeObject(IQueryNode parent, String field) {
    this(parent, field, "asc");
  }

  public SortFieldNodeObject(String field, String order) {
    this(null, field, order);
  }

  public SortFieldNodeObject(String field) {
    this(null, field, "asc");
  }

  private QueryNodeObject createSortConfig() {
    QueryNodeObject config = new QueryNodeObject(this);
    config.put("order", this.order);

    if (this.mode != null) {
      config.put("mode", this.mode);
    }

    if (this.missing != null) {
      config.put("missing", this.missing);
    }

    if (this.numericType != null) {
      config.put("numeric_type", this.numericType);
    }

    return config;
  }

  public void setOrder(String order) {
    this.order = order;
    this.put(field, createSortConfig());
  }

  public void setMode(String mode) {
    this.mode = mode;
    this.put(field, createSortConfig());
  }

  public void setMissing(String missing) {
    this.missing = missing;
    this.put(field, createSortConfig());
  }
}
