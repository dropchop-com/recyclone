package com.dropchop.recyclone.service.api;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 21. 04. 22.
 */
@Slf4j
@ApplicationScoped
public class ServiceSelector {

  public static final String IMPL_CONFIG_PROP = "dropchop.service-type";
  public static final String IMPL_CONFIG_DEFAULT = IMPL_CONFIG_PROP + ".default";

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  Config config;

  @Inject
  @Any
  Instance<Service> sInstances;

  private <S extends Service> S getOrThrow(Class<S> sClass, Instance<S> candidates, String annoValue) {
    Instance<S> sInstance = candidates.select(sClass, new ServiceTypeLiteral(annoValue));
    if (!sInstance.isUnsatisfied()) {
      return sInstance.get();
    }
    throw new RuntimeException("Missing service class [" + sClass + "] with implementation [" + annoValue + "]!");
  }

  public <S extends Service> S select(Class<S> sClass) {
    Instance<S> candidates = sInstances.select(sClass);
    if (candidates.stream().findAny().isEmpty()) {
      throw new RuntimeException("Missing service class [" + sClass + "] implementation!");
    }

    Optional<String> implOpt = config.getOptionalValue(IMPL_CONFIG_PROP + "." + sClass.getSimpleName(), String.class);
    if (implOpt.isPresent()) {
      log.debug("Get service [{}] by implementation name [{}]", sClass, implOpt.get());
      return getOrThrow(sClass, candidates, implOpt.get());
    }

    implOpt = config.getOptionalValue(IMPL_CONFIG_DEFAULT, String.class);
    if (implOpt.isPresent()) {
      log.debug("Get service [{}] by global implementation name [{}].", sClass, implOpt.get());
      return getOrThrow(sClass, candidates, implOpt.get());
    }

    log.debug("Get first available service by class [{}].", sClass);
    return candidates.iterator().next();
  }
}
