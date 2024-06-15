package com.dropchop.recyclone.quarkus.runtime.selectors;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.service.api.Service;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

@Dependent
public class ServiceSelector extends ConfigurableSelector<Service> {

  private final Instance<Service> instances;

  @Inject
  public ServiceSelector(RecycloneBuildConfig config, @Any Instance<Service> instances) {
    super(config);
    this.instances = instances;
  }

  @Override
  public Instance<Service> getInstances() {
    return this.instances;
  }

  @Override
  public <Y extends Service> Y select(Class<Y> dependencyClass, String targetQualifier, String fallbackQualifier) {
    return super.select(dependencyClass, targetQualifier, fallbackQualifier);
  }

  @Override
  public <Y extends Service> Y select(Class<Y> dependencyClass, Class<?> dependantClass) {
    return super.select(dependencyClass, dependantClass);
  }

  @Override
  public <Y extends Service> Y select(Class<Y> dependencyClass, InjectionPoint injectionPoint) {
    return super.select(dependencyClass, injectionPoint);
  }
}
