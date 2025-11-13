package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.query.ConditionOperator;
import com.dropchop.recyclone.base.api.model.query.Condition;
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

  /**
   * Handles the processing of a field that has been constructed in the query building context.
   *
   * @param field    the name of the field being processed
   * @param operator the condition operator associated with the field
   * @param node     the query node object that the field is part of
   */
  public void onBuiltField(String field, ConditionOperator operator, QueryNodeObject node) {
  }

  /**
   * Handles the operations to be performed when a field is constructed in the context of a query.
   *
   * @param field     the name of the field being constructed
   * @param condition the condition associated with the field being built
   * @param node      the query node object that the field is part of
   */
  public void onBuiltField(String field, Condition condition, QueryNodeObject node) {
  }

  @Override
  public void decorate() {
  }
}