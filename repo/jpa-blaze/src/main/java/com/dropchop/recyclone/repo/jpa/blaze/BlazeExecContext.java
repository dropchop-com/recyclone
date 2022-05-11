package com.dropchop.recyclone.repo.jpa.blaze;

import com.blazebit.persistence.CriteriaBuilder;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;
import com.dropchop.recyclone.model.api.invoke.Params;
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
public class BlazeExecContext<E, P extends Params>
  extends ParamsExecContext<P, Listener> implements RepositoryExecContext<E, P> {

  private Iterable<? extends BlazeCriteriaDecorator<E, P>> criteriaDecorators = new ArrayList<>();

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

    for (BlazeCriteriaDecorator<E, P> decorator : criteriaDecorators) {
      decorator.init(this);
    }
  }

  @Override
  public BlazeExecContext<E, P> of(ExecContext<?> sourceContext) {
    super.of(sourceContext);
    return this;
  }

  public BlazeExecContext<E, P> criteriaDecorators(Iterable<? extends BlazeCriteriaDecorator<E, P>> criteriaDecorators) {
    this.setCriteriaDecorators(criteriaDecorators);
    return this;
  }

  @Override
  public BlazeExecContext<E, P> listeners(List<Listener> listeners) {
    super.listeners(listeners);
    return this;
  }

  @Override
  public BlazeExecContext<E, P> listener(Listener listener) {
    super.listener(listener);
    return this;
  }
}
