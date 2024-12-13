package com.dropchop.recyclone.repo.es;

import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;
import com.dropchop.recyclone.mapper.api.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.ParamsExecContext;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class ElasticExecContext<E> extends ParamsExecContext<RepositoryExecContextListener>
  implements RepositoryExecContext<E> {

  @NonNull
  private Class<E> rootClass;

  @NonNull
  private String rootAlias;

  private boolean skipObjectParsing;

  public void init(Class<E> rootClass, String rootAlias) {
    this.rootClass = rootClass;
    this.rootAlias = rootAlias;

    for (RepositoryExecContextListener decorator : listeners()) {
      if (decorator instanceof ElasticCriteriaDecorator elasticCriteriaDecorator) {
        elasticCriteriaDecorator.init(this);
      }
    }
  }

  @Override
  public ElasticExecContext<E> of(ExecContext<?> sourceContext) {
    super.of(sourceContext);
    return this;
  }

  @Override
  public ElasticExecContext<E> listener(RepositoryExecContextListener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  @Override
  public ElasticExecContext<E> totalCount(TotalCountExecContextListener listener) {
    RepositoryExecContext.super.totalCount(listener);
    return this;
  }

  @Override
  public ElasticExecContext<E> decorateWith(CriteriaDecorator listener) {
    RepositoryExecContext.super.decorateWith(listener);
    return this;
  }
}
