package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.ParamsExecContext;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.mapper.api.RepositoryExecContextListener;
import com.dropchop.recyclone.mapper.api.TotalCountExecContextListener;
import lombok.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 03. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class BlazeExecContext<E> extends ParamsExecContext<RepositoryExecContextListener>
  implements RepositoryExecContext<E> {

  @NonNull
  private CriteriaBuilder<E> criteriaBuilder;

  @NonNull
  private Class<E> rootClass;

  @NonNull
  private String rootAlias;


  public void init(Class<E> rootClass, String rootAlias, CriteriaBuilder<E> builder) {
    this.rootClass = rootClass;
    this.rootAlias = rootAlias;
    this.criteriaBuilder = builder;

    for (RepositoryExecContextListener decorator : listeners()) {
      if (decorator instanceof BlazeCriteriaDecorator blazeCriteriaDecorator) {
        blazeCriteriaDecorator.init(this);
      }
    }
  }

  @Override
  public BlazeExecContext<E> of(ExecContext<?> sourceContext) {
    super.of(sourceContext);
    return this;
  }

  @Override
  public BlazeExecContext<E> listener(RepositoryExecContextListener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }

  @Override
  public BlazeExecContext<E> totalCount(TotalCountExecContextListener listener) {
    RepositoryExecContext.super.totalCount(listener);
    return this;
  }

  @Override
  public BlazeExecContext<E> decorateWith(CriteriaDecorator listener) {
    RepositoryExecContext.super.decorateWith(listener);
    return this;
  }
}
