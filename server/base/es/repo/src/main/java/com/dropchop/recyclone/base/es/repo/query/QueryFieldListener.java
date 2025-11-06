package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.ConditionOperator;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 06. 11. 2025.
 */
public interface QueryFieldListener {
  void on(int level, String fieldName, Condition condition, QueryNodeObject node);
  void on(int level, String fieldName, ConditionOperator condition, QueryNodeObject node);
}
