package com.dropchop.recyclone.repo.api.ctx;

import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;
import com.dropchop.recyclone.mapper.api.TotalCountExecContextListener;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.dto.invoke.ParamsExecContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 09. 22.
 */
@SuppressWarnings("unused")
public class BaseRepoExecContext<E> extends ParamsExecContext<RepositoryExecContextListener>
  implements RepositoryExecContext<E> {

  @Override
  public BaseRepoExecContext<E> of(ExecContext<?> sourceContext) {
    super.of(sourceContext);
    return this;
  }

  @Override
  public BaseRepoExecContext<E> listener(RepositoryExecContextListener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  @Override
  public BaseRepoExecContext<E> totalCount(TotalCountExecContextListener listener) {
    RepositoryExecContext.super.totalCount(listener);
    return this;
  }

  @Override
  public BaseRepoExecContext<E> decorateWith(CriteriaDecorator listener) {
    RepositoryExecContext.super.decorateWith(listener);
    return this;
  }
}
