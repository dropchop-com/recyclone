package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 05. 24.
 */
public class FallbackExecContextInitializer extends ExecContextInitializer implements ContainerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(FallbackExecContextInitializer.class);

  @SuppressWarnings("unchecked")
  public FallbackExecContextInitializer(String defaultParamsClassName,
                                        String defaultExecContextClassName,
                                        ExecContextBinder execContextBinder) {
    super(
        execContextBinder,
        (Class<? extends ExecContext<?>>)loadClass(defaultExecContextClassName),
        (Class<? extends Params>)loadClass(defaultParamsClassName),
        com.dropchop.recyclone.base.dto.model.base.Dto.class
    );
    log.debug("Registering fallback REST initializer with [{}, {}, {}]",
        execContextClass,
        paramsClass,
        dataClass
        );
  }
}
