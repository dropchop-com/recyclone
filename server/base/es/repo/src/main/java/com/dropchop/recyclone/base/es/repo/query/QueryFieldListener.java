package com.dropchop.recyclone.base.es.repo.query;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.Field;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 06. 11. 2025.
 */
public interface QueryFieldListener {
  void on(int level, Condition condition, IQueryObject node);
  void on(int level, Field<?> field, IQueryObject node);
}
