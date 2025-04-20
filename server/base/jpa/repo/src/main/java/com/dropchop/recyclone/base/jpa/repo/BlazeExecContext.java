package com.dropchop.recyclone.base.jpa.repo;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.base.api.mapper.TotalCountExecContextListener;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.repo.ctx.BaseRepoExecContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 03. 22.
 */
@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@SuppressWarnings("UnusedReturnValue")
public class BlazeExecContext<E> extends BaseRepoExecContext<E> {

  @NonNull
  private CriteriaBuilder<E> criteriaBuilder;

  public BlazeExecContext(@NonNull Class<E> rootClass, @NonNull String rootAlias) {
    super(rootClass, rootAlias);
  }

  public BlazeExecContext(@NonNull Class<E> rootClass, @NonNull String rootAlias, ExecContext<?> sourceContext) {
    super(rootClass, rootAlias);
    super.of(sourceContext);
  }

  public void init(CriteriaBuilder<E> builder) {
    this.criteriaBuilder = builder;
    super.bindListeners();
  }

  @Override  // Overrides just for proper chaining.
  public BlazeExecContext<E> of(ExecContext<?> sourceContext) {
    super.of(sourceContext);
    return this;
  }

  @Override
  public BlazeExecContext<E> totalCount(TotalCountExecContextListener listener) {
    super.totalCount(listener);
    return this;
  }

  public BlazeExecContext<E> decorateWith(BlazeCriteriaDecorator<E> decorator) {
    return (BlazeExecContext<E>) super.decorateWith(decorator);
  }
}
