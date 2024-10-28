package com.dropchop.recyclone.repo.es;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;
import com.dropchop.recyclone.mapper.api.TotalCountExecContextListener;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.dto.invoke.ParamsExecContext;
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
  private CriteriaBuilder<E> criteriaBuilder;

  @NonNull
  private Class<E> rootClass;

  @NonNull
  private String rootAlias;

  @NonNull
  private String query;


  public void init(Class<E> rootClass, String rootAlias, CriteriaBuilder<E> builder) {
    this.rootClass = rootClass;
    this.rootAlias = rootAlias;
    this.criteriaBuilder = builder;

    for (RepositoryExecContextListener decorator : listeners()) {
      if (decorator instanceof ElasticCriteriaDecorator blazeCriteriaDecorator) {
        blazeCriteriaDecorator.init(this);
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
