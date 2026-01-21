package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.query.operator.filter.Exclude;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import com.dropchop.recyclone.base.api.model.query.operator.filter.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuppressWarnings("unused")
public class TopHitsNodeObject extends QueryNodeObject {
  private Integer size;
  private Integer from;
  private List<SortNodeObject> sort;
  private Filter filter;
  private QueryNodeObject source;

  public TopHitsNodeObject(IQueryNode parent, Integer size) {
    super(parent);
    if (parent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) parent).put("top_hits", this);
    }
    this.size = size;
    this.sort = new ArrayList<>();

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

  @Override
  public void setParent(IQueryNode parent) {
    IQueryNode prevParent = this.getParent();
    if (prevParent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) prevParent).remove("top_hits");
    }
    super.setParent(parent);
    if (parent instanceof IQueryNodeObject) {
      ((IQueryNodeObject) parent).put("top_hits", this);
    }
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
    SortNodeObject sortNode = new SortNodeObject(this, field, order, numericType);
    this.sort.add(sortNode);
    updateSortArray();
  }

  public void addSort(String field, String order) {
    SortNodeObject sortNode = new SortNodeObject(this, field, order);
    this.sort.add(sortNode);
    updateSortArray();
  }

  public void addSort(String field) {
    addSort(field, "asc");
  }

  public void clearSort() {
    this.sort.clear();
    this.remove("sort");
  }

  private void updateSortArray() {
    if (!sort.isEmpty()) {
      QueryNodeList sortList = new QueryNodeList(this);
      sortList.addAll(sort);

      this.put("sort", sortList);
    }
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
