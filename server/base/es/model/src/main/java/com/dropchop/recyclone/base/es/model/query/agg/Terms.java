package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import lombok.Getter;

import java.util.List;

@Getter
@SuppressWarnings("unused")
public class Terms extends AggregationBucket {
  private String field;
  private Integer size;
  private Object include;
  private Object exclude;

  public Terms(IQueryNode parent, String field) {
    super(parent, "terms");
    setField(field);
  }

  public Terms(String field) {
    this(null, field);
  }

  public Terms() {
    this(null, null);
  }

  public void setField(String field) {
    this.field = field;
    if (field == null) {
      body.remove("field");
    } else {
      body.put("field", field);
    }
  }

  public void setSize(Integer size) {
    this.size = size;
    if (size == null) {
      body.remove("size");
    } else {
      body.put("size", size);
    }
  }

  public void setInclude(List<String> includes) {
    Object value = toIncludeExcludeValue(includes);
    this.include = value;
    if (value == null) {
      body.remove("include");
    } else {
      body.put("include", value);
    }
  }

  public void setExclude(List<String> excludes) {
    Object value = toIncludeExcludeValue(excludes);
    this.exclude = value;
    if (value == null) {
      body.remove("exclude");
    } else {
      body.put("exclude", value);
    }
  }

  public void setFilter(Filter filter) {
    if (filter == null) {
      setInclude(null);
      setExclude(null);
      return;
    }
    if (filter.getInclude() != null) {
      setInclude(filter.getInclude().getValue());
    }
    if (filter.getExclude() != null) {
      setExclude(filter.getExclude().getValue());
    }
  }

  private static Object toIncludeExcludeValue(List<String> values) {
    if (values == null || values.isEmpty()) {
      return null;
    }
    if (values.size() == 1) {
      String single = values.getFirst();
      if (single != null && single.contains("*")) {
        return single;
      }
    }
    return values;
  }
}
