package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.query.ConditionOperator;
import com.dropchop.recyclone.base.api.repo.ctx.CriteriaDecorator;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class ElasticCriteriaDecorator<E> implements CriteriaDecorator<E, ElasticExecContext<E>> {
  private ElasticExecContext<?> context;

  @Override
  public void init(ElasticExecContext<E> executionContext) {
    this.context = executionContext;
  }

  public abstract void onBuiltField(String field, ConditionOperator operator, QueryNodeObject node);
}