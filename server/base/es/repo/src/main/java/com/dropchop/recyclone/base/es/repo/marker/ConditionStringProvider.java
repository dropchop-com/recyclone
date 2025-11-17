package com.dropchop.recyclone.base.es.repo.marker;

import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.IQueryNodeObject;

import java.util.List;

@SuppressWarnings("unused")
public interface ConditionStringProvider {

  IQueryNodeObject provideCondition(QueryParams params);
  IQueryNodeObject provideCondition(QueryParams params, List<CriteriaDecorator<?, ?>> decorators);

  IQueryNodeObject provideConditionWithMaxSizeWithoutAggregation(QueryParams params);
  IQueryNodeObject provideConditionWithMaxSizeWithoutAggregation(QueryParams queryParams,
                                                                 List<CriteriaDecorator<?, ?>> decorators);

  String encodeCondition(IQueryNodeObject queryNodeObject);
}
