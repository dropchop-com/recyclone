package com.dropchop.recyclone.quarkus.runtime.service;

import com.dropchop.recyclone.service.api.Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class ServiceInjectResolver {

  @Inject
  @Any
  Instance<Service> services;

  public <S extends Service> S service(Class<S> sClass) {
    Instance<S> candidates = services.select(sClass);
    if (candidates.stream().findAny().isEmpty()) {
      throw new RuntimeException("Missing service class [" + sClass + "] implementation!");
    }
    return candidates.iterator().next();
  }
}
