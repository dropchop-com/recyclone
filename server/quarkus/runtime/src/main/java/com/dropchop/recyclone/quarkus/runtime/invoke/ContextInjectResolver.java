package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 04. 24.
 */
@Singleton
public class ContextInjectResolver {

  @Any
  @Inject
  Instance<ExecContext<?>> execContextInstances;


}
