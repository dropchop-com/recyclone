package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.service.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.service.api.invoke.ExecContextProviderProducer;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;

/**
 * ContainerRequestFilter which initializes {@link com.dropchop.recyclone.model.api.invoke.ExecContext} from
 * JAX-RS Recyclone internal context variables.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
@ConstrainedTo(RuntimeType.SERVER)
public class ExecContextInitInterceptor implements ContainerRequestFilter {

  private final ExecContextProviderProducer execContextProviderProducer;
  private final Class<?> execContextClass;

  public ExecContextInitInterceptor(Class<?> execContextClass, ExecContextProviderProducer execContextProviderProducer) {
    log.trace("ExecContextInitInterceptor2 constructor");
    this.execContextProviderProducer = execContextProviderProducer;
    this.execContextClass = execContextClass;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    ExecContextProvider execContextProvider = execContextProviderProducer.getExecContextProvider(this.execContextClass);
    log.debug("Creating execution context class [{}] with provider [{}].", this.execContextClass, execContextProvider);
    UriInfo info = requestContext.getUriInfo();
    execContextProvider.create(info);
    requestContext.setProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER, execContextProvider);
  }
}


