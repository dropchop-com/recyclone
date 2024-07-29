package com.dropchop.recyclone.model.es.api.query;
import java.util.List;

public interface QueryFilter<T> {
  String getField();
  Operation getOperation();
  T getValue();
  List<QueryFilter<T>> getSubFilters();
  LogicalOperator getLogicalOperator();

  enum Operation {
    EQUAL, GREATER_THAN, LESS_THAN, IN, EXISTS, RANGE
  }

  enum LogicalOperator {
    AND, OR, NOT
  }
}
