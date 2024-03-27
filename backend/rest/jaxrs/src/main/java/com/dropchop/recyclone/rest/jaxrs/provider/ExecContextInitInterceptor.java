package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.invoke.*;
import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_REQUEST_PATH;

/**
 * ContainerRequestFilter which initializes {@link com.dropchop.recyclone.model.api.invoke.ExecContext} from
 * JAX-RS Recyclone internal context variables.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
@ConstrainedTo(RuntimeType.SERVER)
public class ExecContextInitInterceptor implements ContainerRequestFilter {

  private final ExecContextContainerProvider contextContainerProvider;
  private final Class<?> execContextClass;

  public ExecContextInitInterceptor(Class<?> execContextClass, ExecContextContainerProvider execContextProviderProducer) {
    log.trace("ExecContextInitInterceptor2 constructor");
    this.contextContainerProvider = execContextProviderProducer;
    this.execContextClass = execContextClass;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    ExecContextContainer contextContainer = contextContainerProvider.getExecContextProvider(this.execContextClass);
    log.debug("Creating execution context class [{}] with provider [{}].", this.execContextClass, contextContainer);
    contextContainer.createContext();
    requestContext.setProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER, contextContainer);
    MDC.put(MDC_REQUEST_PATH, requestContext.getUriInfo().getPath());
  }
}


