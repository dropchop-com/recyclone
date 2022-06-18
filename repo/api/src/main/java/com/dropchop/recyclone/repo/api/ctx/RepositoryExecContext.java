package com.dropchop.recyclone.repo.api.ctx;

import com.dropchop.recyclone.model.api.invoke.ParamsExecContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 22.
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
