package com.dropchop.recyclone.repo.api.ctx;

import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;
import com.dropchop.recyclone.mapper.api.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.invoke.ParamsExecContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 03. 22.
 */
public interface RepositoryExecContext<E> extends ParamsExecContext<RepositoryExecContextListener> {
  @Override
  default RepositoryExecContext<E> listener(RepositoryExecContextListener listener) {
    ParamsExecContext.super.listener(listener);
    return this;
  }

  default RepositoryExecContext<E> totalCount(TotalCountExecContextListener listener) {
    ParamsExecContext.super.listener(listener);
    return this;
  }

  default RepositoryExecContext<E> decorateWith(CriteriaDecorator listener) {
    ParamsExecContext.super.listener(listener);
    return this;
  }
}
