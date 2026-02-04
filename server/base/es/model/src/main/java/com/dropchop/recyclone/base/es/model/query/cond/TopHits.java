package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.api.model.query.operator.filter.Exclude;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Include;
import com.dropchop.recyclone.base.es.model.query.*;
import com.dropchop.recyclone.base.es.model.query.SortField.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class TopHits extends QueryObject {
  private Integer size;
  private Integer from;
  private Filter filter;
  private IQueryObject source;

  private final Sort sort = new Sort(this);

  public TopHits(IQueryNode parent, Integer size) {
    super(parent);
    this.size = size;
    if (size != null) {
      this.put("size", size);
    }
  }

  public TopHits(IQueryNode parent) {
    this(parent, null);
  }

  public TopHits() {
    this(null);
  }

  public void setSize(Integer size) {
    this.size = size;
    if (size != null) {
      this.put("size", size);
    } else {
      this.remove("size");
    }
  }

  public void setFrom(Integer from) {
    this.from = from;
    if (from != null) {
      this.put("from", from);
    } else {
      this.remove("from");
    }
  }

  public void addSort(String field, Order order, String numericType) {
    this.sort.addSort(field, order, numericType);
    this.putAll(this.sort);
  }

  public void addSort(String field, Order order) {
    this.sort.addSort(field, order);
    this.putAll(this.sort);
  }

  public void addSort(String field) {
    addSort(field, Order.ASC);
  }

  public void clearSort() {
    this.sort.clear();
    this.remove("sort");
  }

  public void setSource(Include includes, Exclude excludes) {
    QueryObject sourceConfig = new QueryObject(this);

    if (includes != null) {
      sourceConfig.put("includes", includes.getValue());
    }

    if (excludes != null) {
      sourceConfig.put("excludes", excludes.getValue());
    }

    this.source = sourceConfig;
    this.put("_source", sourceConfig);
  }

  public void setSourceIncludes(Include includes) {
    setSource(includes, null);
  }

  public void setSourceExcludes(Exclude excludes) {
    setSource(null, excludes);
  }
}
