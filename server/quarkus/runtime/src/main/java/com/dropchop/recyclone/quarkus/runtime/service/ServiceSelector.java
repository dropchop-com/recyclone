package com.dropchop.recyclone.quarkus.runtime.service;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.quarkus.runtime.common.Selector;
import com.dropchop.recyclone.service.api.Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServiceSelector implements Selector<Service> {

  @Inject
  @Any
  Instance<Service> instances;

  public <S extends Service> S service(Class<S> sClass) {
    Instance<S> candidates = instances.select(sClass);
    if (candidates.stream().findAny().isEmpty()) {
      throw new RuntimeException("Missing service class [" + sClass + "] implementation!");
    }
    return candidates.iterator().next();
  }

  @Override
  public Instance<Service> getInstances() {
    return instances;
  }

  @Override
  public Class<Service> getBase() {
    return this.getClassFromName(ExecContext.class.getName());
  }
}
