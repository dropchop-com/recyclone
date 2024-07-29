package com.dropchop.recyclone.model.es.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryFilter<T> implements com.dropchop.recyclone.model.es.api.query.QueryFilter {
  private String field;
  private Operation operation;
  private T value;
  private LogicalOperator logicalOperator;

  @Override
  public List<com.dropchop.recyclone.model.es.api.query.QueryFilter> getSubFilters() {
    return Collections.emptyList();
  }
}