package com.dropchop.shiro.jaxrs;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.recyclone.service.api.invoke.ExecContextProvider;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
public class ShiroContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private final Map<AuthorizingAnnotationHandler, Annotation> authzChecks;

  private final ShiroAuthorizationService authorizationService;

  private static AuthorizingAnnotationHandler createHandler(Annotation annotation) {
    Class<?> t = annotation.annotationType();
    if (RequiresPermissions.class.equals(t)) {
      return new PermissionAnnotationHandler();
    }
    /*else if (RequiresRoles.class.equals(t)) return new RoleAnnotationHandler();
    else if (RequiresUser.class.equals(t)) return new UserAnnotationHandler();
    else if (RequiresGuest.class.equals(t)) return new GuestAnnotationHandler();
    else if (RequiresAuthentication.class.equals(t)) return new AuthenticatedAnnotationHandler();*/
    else throw new IllegalArgumentException("Cannot create a handler for the unknown for annotation " + t);
  }

  public ShiroContainerFilter(ShiroAuthorizationService authorizationService,
                              Collection<Annotation> authzSpecs,
                              String resourceClassName, String resourceMethodName) {
    Map<AuthorizingAnnotationHandler, Annotation> authChecks = new HashMap<>(authzSpecs.size());
    for (Annotation authSpec : authzSpecs) {
      authChecks.put(createHandler(authSpec), authSpec);
    }
    this.authorizationService = authorizationService;
    this.authzChecks = Collections.unmodifiableMap(authChecks);
    log.debug("Constructed {} for [{}:{}] with [{}]",
      this.getClass().getName(), resourceClassName, resourceMethodName, authzSpecs);
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    this.authorizationService.bindSubjectToThreadStateInRequestContext(requestContext);
    this.authorizationService.invokeFilterChain(requestContext);
    ExecContextProvider execContextProvider = (ExecContextProvider)requestContext
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContextProvider == null) {
      log.warn("Missing {} in {}!", ExecContextProvider.class.getSimpleName(), ContainerRequestContext.class.getSimpleName());
      return;
    }
    this.authorizationService.extractRequiredPermissionsToExecContext(execContextProvider.get(), authzChecks);
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    this.authorizationService.unbindSubjectFromThreadStateInRequestContext(requestContext);
  }
}
