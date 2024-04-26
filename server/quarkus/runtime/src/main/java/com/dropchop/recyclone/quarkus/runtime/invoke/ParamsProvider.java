package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
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
public class ParamsProvider implements Factory<Params>, Selector<Params> {

  @Any
  @Inject
  Instance<Params> instances;

  @Override
  public Instance<Params> getInstances() {
    return instances;
  }

  @Override
  public Class<Params> getBase() {
    return this.getClassFromName(ExecContext.class.getName());
  }
}
