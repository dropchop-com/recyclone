package com.dropchop.recyclone.base.api.repo.ctx;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.mapper.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.invoke.ParamsExecContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 03. 22.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
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

  default <R extends RepositoryExecContext<E>> RepositoryExecContext<E> decorateWith(CriteriaDecorator<E, R> listener) {
    ParamsExecContext.super.listener(listener);
    return this;
  }
}
