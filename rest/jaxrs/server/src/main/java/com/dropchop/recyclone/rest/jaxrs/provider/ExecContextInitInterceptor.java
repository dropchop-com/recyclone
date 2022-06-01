package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.service.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.service.api.ExecContextType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.Priorities;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 * ContainerRequestFilter which initializes {@link com.dropchop.recyclone.model.api.invoke.ExecContext} from
 * JAX-RS Recyclone internal context variables.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
@Provider
@Priority(Priorities.AUTHENTICATION)
@ConstrainedTo(RuntimeType.SERVER)
public class ExecContextInitInterceptor implements ContainerRequestFilter {

  @Inject
  @RequestScoped
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ExecContextProvider execContextProvider;

  public ExecContextInitInterceptor() {
    log.error("ExecContextInitInterceptor constructor");
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    log.debug("filter");
    execContextProvider.create();
    requestContext.setProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER, execContextProvider);
  }


  /*
  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

    Params params = (Params) context.getProperty(InternalContextVariables.RECYCLONE_PARAMS);
    if (params != null) {
      execContextProvider.setParams(params);
    }
    @SuppressWarnings("unchecked")
    List<Dto> dtos = (List<Dto>) context.getProperty(InternalContextVariables.RECYCLONE_DATA);
    if (dtos != null) {
      execContextProvider.get().setData(dtos);
    }
    Subject subject = (Subject) context.getProperty(InternalContextVariables.RECYCLONE_SECURITY_SUBJECT);
    if (subject != null) {
      execContextProvider.get().setSubject(subject);
    }
    String securityDomain = (String) context.getProperty(InternalContextVariables.RECYCLONE_SECURITY_DOMAIN);
    if (securityDomain != null) {
      execContextProvider.get().setSecurityDomain(securityDomain);
    }
    String securityAction = (String) context.getProperty(InternalContextVariables.RECYCLONE_SECURITY_ACTION);
    if (securityAction != null) {
      execContextProvider.get().setSecurityAction(securityAction);
    }

    @SuppressWarnings("unchecked")
    List<String> requiredPermissions = (List<String>) context
      .getProperty(InternalContextVariables.RECYCLONE_SECURITY_REQUIRED_PERM);
    if (requiredPermissions != null) {
      execContextProvider.get().setRequiredPermissions(requiredPermissions);
    }

    Logical requiredPermissionsOp = (Logical) context
      .getProperty(InternalContextVariables.RECYCLONE_SECURITY_REQUIRED_PERM_OP);
    if (requiredPermissionsOp != null) {
      execContextProvider.get().setRequiredPermissionsOp(requiredPermissionsOp);
    }

    return context.proceed();
  }*/
}


