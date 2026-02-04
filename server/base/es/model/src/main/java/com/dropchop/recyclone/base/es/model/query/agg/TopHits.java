package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.api.model.query.operator.filter.Exclude;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Include;
import com.dropchop.recyclone.base.es.model.query.*;
import com.dropchop.recyclone.base.es.model.query.SortField.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@SuppressWarnings("unused")
public class TopHits extends Aggregation {
  private Integer size;
  private Integer from;
  private Filter filter;
  private IQueryObject source;

  private final Sort sort = new Sort(this);

  public TopHits(IQueryNode parent, Integer size,
                 List<com.dropchop.recyclone.base.api.model.query.aggregation.Sort> sort, Filter filter) {
    super(parent, "top_hits");
    setSize(size);
    if (sort != null) {
      for (com.dropchop.recyclone.base.api.model.query.aggregation.Sort s : sort) {
        this.addSort(s.getField(), Order.valueOf(s.getValue().toUpperCase()), s.getNumericType());
      }
    }
    if (filter != null && filter.getInclude() != null) {
      this.setSourceIncludes(filter.getInclude());
    }
  }

  public void setSize(Integer size) {
    this.size = size;
    if (size != null) {
      body.put("size", size);
    } else {
      body.remove("size");
    }
  }

  public void setFrom(Integer from) {
    this.from = from;
    if (from != null) {
      body.put("from", from);
    } else {
      body.remove("from");
    }
  }

  public void addSort(String field, Order order, String numericType) {
    this.sort.addSort(field, order, numericType);
    body.putAll(this.sort);
  }

  public void addSort(String field, Order order) {
    this.sort.addSort(field, order);
    body.putAll(this.sort);
  }

  public void addSort(String field) {
    addSort(field, Order.ASC);
  }

  public void clearSort() {
    this.sort.clear();
    body.remove("sort");
  }

  public void setSource(Include includes, Exclude excludes) {
    QueryObject sourceConfig = new QueryObject(this);

    if (includes != null) {
      sourceConfig.put("includes", includes.getValue());
    }

    if (excludes != null) {
      sourceConfig.put("excludes", excludes.getValue());
    }

    if (sourceConfig.isEmpty()) {
      this.source = null;
      body.remove("_source");
    } else {
      this.source = sourceConfig;
      body.put("_source", sourceConfig);
    }
  }

  public void setSourceIncludes(Include includes) {
    setSource(includes, null);
  }

  public void setSourceExcludes(Exclude excludes) {
    setSource(null, excludes);
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
    if (filter == null) {
      setSource(null, null);
    } else {
      setSource(filter.getInclude(), filter.getExclude());
    }
  }
}
