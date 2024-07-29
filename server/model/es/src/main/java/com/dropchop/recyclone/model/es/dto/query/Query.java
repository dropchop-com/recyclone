package com.dropchop.recyclone.model.es.dto.query;

import com.dropchop.recyclone.model.es.api.query.QueryAggregation;
import com.dropchop.recyclone.model.es.api.query.QueryFilter;

import java.util.Collections;
import java.util.List;


public class Query implements com.dropchop.recyclone.model.es.api.query.Query {
  private final List<QueryFilter<?>> filters;
  private final List<QueryAggregation> aggregations;

  public Query(List<QueryFilter<?>> filters, List<QueryAggregation> aggregations) {
    this.filters = Collections.unmodifiableList(filters);
    this.aggregations = Collections.unmodifiableList(aggregations);
  }

  @Override
  public List<QueryFilter<?>> getFilters() {
    return filters;
  }

  @Override
  public List<QueryAggregation> getAggregation() {
    return aggregations;
  }
}
