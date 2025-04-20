package com.dropchop.recyclone.base.api.repo.ctx;

import com.dropchop.recyclone.base.api.mapper.RepositoryExecContextListener;
import com.dropchop.recyclone.base.api.mapper.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.ParamsExecContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 09. 22.
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class BaseRepoExecContext<E> extends ParamsExecContext<RepositoryExecContextListener>
  implements RepositoryExecContext<E> {

  @NonNull
  private Class<E> rootClass;

  @NonNull
  private String rootAlias;

  public BaseRepoExecContext(Class<E> rootClass, String rootAlias) {
    this.rootClass = rootClass;
    this.rootAlias = rootAlias;
  }

  public void bindListeners() {
    for (RepositoryExecContextListener decorator : listeners()) {
      if (!(decorator instanceof CriteriaDecorator<?, ?>)) {
        continue;
      }
      @SuppressWarnings("unchecked")
      CriteriaDecorator<E, RepositoryExecContext<E>> cDecorator =
          (CriteriaDecorator<E, RepositoryExecContext<E>>) decorator;
      cDecorator.init(this);
    }
  }

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
  public  <R extends RepositoryExecContext<E>> BaseRepoExecContext<E> decorateWith(CriteriaDecorator<E, R> listener) {
    RepositoryExecContext.super.decorateWith(listener);
    return this;
  }
}
