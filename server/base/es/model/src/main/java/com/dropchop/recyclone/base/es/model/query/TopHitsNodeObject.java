package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.query.operator.filter.Exclude;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Include;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class TopHitsNodeObject extends QueryNodeObject {
  private Integer size;
  private Integer from;
  private Filter filter;
  private QueryNodeObject source;

  private final SortNodeObject sort = new SortNodeObject(this);

  public TopHitsNodeObject(IQueryNode parent, Integer size) {
    super(parent);
    this.size = size;
    if (size != null) {
      this.put("size", size);
    }
  }

  public TopHitsNodeObject(IQueryNode parent) {
    this(parent, null);
  }

  public TopHitsNodeObject() {
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

  public void addSort(String field, String order, String numericType) {
    this.sort.addSort(field, order, numericType);
    this.putAll(this.sort);
  }

  public void addSort(String field, String order) {
    this.sort.addSort(field, order);
    this.putAll(this.sort);
  }

  public void addSort(String field) {
    addSort(field, "asc");
  }

  public void clearSort() {
    this.sort.clear();
    this.remove("sort");
  }

  public void setSource(Include includes, Exclude excludes) {
    QueryNodeObject sourceConfig = new QueryNodeObject(this);

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
