package com.dropchop.recyclone.model.api.query;
import java.util.List;

public interface HasAggregations {
  List<AggregationCriteria> getAggregations();
  void setAggregations(List<AggregationCriteria> aggregations);
}
