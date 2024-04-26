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

  /**
   * Keep for CDI to work
   */
  @Override
  public <P extends Params> P create(Class<P> clazz) {
    return Factory.super.create(clazz);
  }

  /**
   * Keep for CDI to work
   */
  @Override
  public <P extends Params> P create(String paramsClassName) {
    return Factory.super.create(paramsClassName);
  }

  /**
   * Keep for CDI to work
   */
  @Override
  public <P extends Params> P select(Class<P> clazz) {
    return Selector.super.select(clazz);
  }

  /**
   * Keep for CDI to work
   */
  @Override
  public <P extends Params> P select(String className) {
    return Selector.super.select(className);
  }

  /**
   * Keep for CDI to work
   */
  @Override
  public <P extends Params> Class<P> getClassFromName(String clazzName) {
    return Factory.super.getClassFromName(clazzName);
  }
}
