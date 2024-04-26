package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.quarkus.runtime.common.Factory;
import com.dropchop.recyclone.quarkus.runtime.common.Selector;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 04. 24.
 */
@RequestScoped
public class ExecContextProvider implements Factory<ExecContext<?>>, Selector<ExecContext<?>> {

  @Any
  @Inject
  Instance<ExecContext<?>> instances;

  @Override
  public Instance<ExecContext<?>> getInstances() {
    return instances;
  }

  @Override
  public Class<ExecContext<?>> getBase() {
    return this.getClassFromName(ExecContext.class.getName());
  }
}
