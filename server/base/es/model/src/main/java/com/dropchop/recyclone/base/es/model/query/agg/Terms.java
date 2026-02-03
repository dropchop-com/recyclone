package com.dropchop.recyclone.base.es.model.query.agg;

import com.dropchop.recyclone.base.api.model.query.operator.filter.Filter;
import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import lombok.Getter;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 01. 2026.
 */
@Getter
@SuppressWarnings("unused")
public class Terms extends AggregationBucket {
  private String field;
  private Integer size;
  private Integer shardSize;
  private List<String> includes;
  private List<String> excludes;

  public Terms(IQueryNode parent, String field, Integer size, Integer shardSize, Filter filter) {
    super(parent, "terms");
    setField(field);
    setSize(size);
    setShardSize(shardSize);
    if (filter == null) {
      setIncludes(null);
      setExcludes(null);
    } else {
      if (filter.getInclude() != null) {
        setIncludes(filter.getInclude().getValue());
      }
      if (filter.getExclude() != null) {
        setExcludes(filter.getExclude().getValue());
      }
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

  public void setShardSize(Integer shardSize) {
    this.shardSize = shardSize;
    if (shardSize == null) {
      body.remove("shard_size");
    } else {
      body.put("shard_size", shardSize);
    }
  }

  public void setField(String field) {
    this.field = field;
    if (field == null) {
      body.remove("field");
    } else {
      body.put("field", field);
    }
  }

  public void setIncludes(List<String> includes) {
    Object value = toIncludeExcludeValue(includes);
    this.includes = includes;
    if (value == null) {
      body.remove("include");
    } else {
      body.put("include", value);
    }
  }

  public void setExcludes(List<String> excludes) {
    Object value = toIncludeExcludeValue(excludes);
    this.excludes = excludes;
    if (value == null) {
      body.remove("exclude");
    } else {
      body.put("exclude", value);
    }
  }

  public void setFilter(Filter filter) {
    if (filter == null) {
      setIncludes(null);
      setExcludes(null);
      return;
    }
    if (filter.getInclude() != null) {
      setIncludes(filter.getInclude().getValue());
    }
    if (filter.getExclude() != null) {
      setExcludes(filter.getExclude().getValue());
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
