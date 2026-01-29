package com.dropchop.recyclone.base.es.repo.marker;

import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;

import java.util.List;

@SuppressWarnings("unused")
public interface ConditionStringProvider {

  IQueryObject provideCondition(QueryParams params);
  IQueryObject provideCondition(QueryParams params, List<CriteriaDecorator<?, ?>> decorators);

  IQueryObject provideConditionWithMaxSizeWithoutAggregation(QueryParams params);
  IQueryObject provideConditionWithMaxSizeWithoutAggregation(QueryParams queryParams,
                                                             List<CriteriaDecorator<?, ?>> decorators);

  String encodeCondition(IQueryObject queryNodeObject);
}
