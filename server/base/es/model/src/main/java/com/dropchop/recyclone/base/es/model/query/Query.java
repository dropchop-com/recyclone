package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.base.api.model.query.Aggregation;
import com.dropchop.recyclone.base.api.model.query.aggregation.AggregationList;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 05. 02. 2026.
 */
@Getter
public class Query extends QueryObject {

  private Integer from = 0;
  private Integer size;
  private Sort sort;
  private ContentFilter filter;
  private IQueryObject query;
  private QueryObject aggs;

  public void setFrom(Integer from) {
    this.from = from;
    if (from != null) {
      this.put("from", from);
    } else {
      this.remove("from");
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

  public void setSort(Sort sort) {
    this.sort = sort;
    if (sort != null) {
      this.putAll(sort);
    } else {
      this.remove("sort");
    }
  }

  public void setFilter(ContentFilter filter) {
    this.filter = filter;
    if (filter != null) {
      QueryObject source = new QueryObject();
      List<String> excludes = filter.getExcludes();
      if (excludes != null && !excludes.isEmpty()) {
        source.put("excludes", excludes);
      }
      List<String> includes = filter.getIncludes();
      if (includes != null && !includes.isEmpty()) {
        source.put("includes", includes);
      }
      if (!source.isEmpty()) {
        this.put("_source", source);
      }
    } else {
      this.remove("_source");
    }
  }

  public void setQuery(IQueryObject query) {
    this.query = query;
    if (query != null) {
      this.put("query", query);
    } else {
      this.remove("query");
    }
  }

  @SuppressWarnings("unused")
  public void setAggs(QueryObject aggs) {
    this.aggs = aggs;
    if (aggs != null) {
      this.put("aggs", aggs);
    } else {
      this.remove("aggs");
    }
  }

  public void setAggs(AggregationList aggsList, Function<Aggregation, IQueryObject> aggsBuilder) {
    if (aggsList != null && !aggsList.isEmpty()) {
      this.aggs = new QueryObject();
      for (Aggregation agg : aggsList) {
        if (agg instanceof Aggregation.Wrapper) {
          // unwrap Aggregation
          agg = ((Aggregation.Wrapper) agg).iterator().next();
        }
        IQueryObject aggObj = aggsBuilder.apply(agg);
        this.aggs.put(agg.getName(), aggObj);
      }
      this.put("aggs", aggs);
    } else {
      this.remove("aggs");
    }
  }
}
