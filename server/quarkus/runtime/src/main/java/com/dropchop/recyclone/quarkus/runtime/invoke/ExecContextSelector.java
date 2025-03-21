package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.quarkus.runtime.selectors.ParameterizedSelector;
import jakarta.enterprise.context.RequestScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 04. 24.
 */
@RequestScoped
public class ExecContextSelector implements ParameterizedSelector<ExecContext<?>, Dto> {

  /**
   * Keep this override for CDI to work
   */
  @Override
  public <R extends ExecContext<?>, P extends Dto> R select(Class<R> rawClass, Class<P> parameterClass) {
    return ParameterizedSelector.super.select(rawClass, parameterClass);
  }
}
