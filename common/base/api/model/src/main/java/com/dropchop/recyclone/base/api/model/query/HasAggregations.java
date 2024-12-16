package com.dropchop.recyclone.base.api.model.query;
import java.util.List;

public interface HasAggregations {
  List<AggregationCriteria> getAggregations();
  void setAggregations(List<AggregationCriteria> aggregations);
}
