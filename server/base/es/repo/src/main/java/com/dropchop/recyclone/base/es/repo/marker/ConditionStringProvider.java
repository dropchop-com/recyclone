package com.dropchop.recyclone.base.es.repo.marker;

import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.IQueryNodeObject;

public interface ConditionStringProvider {
  IQueryNodeObject provideCondition(QueryParams params);
  IQueryNodeObject provideConditionWithMaxSizeWithoutAggregation(QueryParams params);
  String encodeCondition(IQueryNodeObject queryNodeObject);
}
