package com.dropchop.recyclone.model.es.api.query;

import java.util.List;

public interface Query {
  List<QueryFilter<?>> getFilters();
  List<QueryAggregation> getAggregation();
}
