package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.repo.api.Repository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 24. 05. 24.
 */
@ApplicationScoped
public class RepositorySelector {

  public static final String IMPL_CONFIG_PROP = "quarkus.recyclone.service";
  public static final String IMPL_CONFIG_DEFAULT = IMPL_CONFIG_PROP + ".qualifier";

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  Config config;

  /*@Inject
  @Any
  Instance<Repository<?, ?>> rInstances;

  public <E, ID, R extends Repository<E, ID>, S extends Service> R select(Class<R> repoClass, Class<S> serviceClass) {
    Instance<R> candidates = rInstances.select(repoClass);
    if (candidates.stream().findAny().isEmpty()) {
      throw new RuntimeException("Missing service class [" + repoClass + "] implementation!");
    }

    return null;
  }*/
}
