package com.dropchop.recyclone.base.es.model.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class SortField extends QueryObject {

  public enum Order {ASC, DESC}

  private String field;
  private Order order;
  private String mode;
  private String missing;
  private String numericType;

  public SortField(IQueryNode parent, String field, Order order, String numericType) {
    super(parent);
    this.field = field;
    this.order = order;
    this.numericType = numericType;
    this.put(field, createSortConfig());
  }

  public SortField(IQueryNode parent, String field, Order order) {
    this(parent, field, order, null);
  }

  public SortField(IQueryNode parent, String field) {
    this(parent, field, Order.ASC);
  }

  public SortField(String field, Order order) {
    this(null, field, order);
  }

  public SortField(String field) {
    this(null, field, Order.ASC);
  }

  private QueryObject createSortConfig() {
    QueryObject config = new QueryObject(this);
    config.put("order", this.order.toString().toLowerCase());

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

  public void setOrder(Order order) {
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
