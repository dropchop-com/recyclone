package com.dropchop.recyclone.quarkus.runtime.service;

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

  /**
   * Here we can select service by an interface also, not only by concrete class.
   */
  public <S extends Service> S select(Class<S> sClass) {
    S service = Selector.super.select(sClass);
    if (service != null) {
      return service;
    }
    Instance<S> candidates = instances.select(sClass);
    if (candidates.stream().findAny().isEmpty()) {
      throw new RuntimeException("Missing service class [" + sClass + "] implementation!");
    }
    return candidates.iterator().next();
  }
}
