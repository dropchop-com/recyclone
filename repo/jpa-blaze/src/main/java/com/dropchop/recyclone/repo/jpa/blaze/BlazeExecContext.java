package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;
import com.dropchop.recyclone.model.dto.invoke.ParamsExecContext;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 03. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class BlazeExecContext<E>
  extends ParamsExecContext<Listener> implements RepositoryExecContext<E> {

  private Iterable<? extends BlazeCriteriaDecorator<E>> criteriaDecorators = new ArrayList<>();

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

    for (BlazeCriteriaDecorator<E> decorator : criteriaDecorators) {
      decorator.init(this);
    }
  }

  @Override
  public BlazeExecContext<E> of(ExecContext<?> sourceContext) {
    super.of(sourceContext);
    return this;
  }

  public BlazeExecContext<E> criteriaDecorators(Iterable<? extends BlazeCriteriaDecorator<E>> criteriaDecorators) {
    this.setCriteriaDecorators(criteriaDecorators);
    return this;
  }

  @Override
  public BlazeExecContext<E> listeners(List<Listener> listeners) {
    super.listeners(listeners);
    return this;
  }

  @Override
  public BlazeExecContext<E> listener(Listener listener) {
    if (listener == null) {
      return this;
    }
    super.listener(listener);
    return this;
  }
}
