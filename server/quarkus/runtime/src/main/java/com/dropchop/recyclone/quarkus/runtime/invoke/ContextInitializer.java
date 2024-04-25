package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.ParamsExecContext;
import com.dropchop.recyclone.model.dto.invoke.Params;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 04. 24.
 */
@ApplicationScoped
public class ContextInitializer {

  @Any
  @Inject
  Instance<Params> paramsInstances;

  //@Produces
  //@RequestScoped
  //ParamsExecContext<?> initContext()
}
