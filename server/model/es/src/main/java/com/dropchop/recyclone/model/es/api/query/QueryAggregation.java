package com.dropchop.recyclone.model.es.api.query;

public interface QueryAggregation {
  String getField();
  AggregateType getType();

  enum AggregateType {
    SUM, AVG, COUNT, MAX, MIN
  }
}
