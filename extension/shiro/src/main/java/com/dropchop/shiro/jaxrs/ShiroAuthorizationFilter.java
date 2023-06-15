package com.dropchop.shiro.jaxrs;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.SecurityExecContext;
import com.dropchop.recyclone.model.api.security.annotations.*;
import com.dropchop.recyclone.model.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.shiro.aop.*;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.subject.Subject;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_PERSON_ID;
import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_PERSON_NAME;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
public class ShiroAuthorizationFilter implements ContainerResponseFilter {

  private final Map<AuthorizingAnnotationHandler, Annotation> authzChecks;

  private final ShiroAuthorizationService authorizationService;

  private static AuthorizingAnnotationHandler createHandler(Annotation annotation) {
    Class<?> t = annotation.annotationType();
    if (RequiresPermissions.class.equals(t)) {
      return new PermissionAnnotationHandler();
    } else if (RequiresRoles.class.equals(t)) {
      return new RoleAnnotationHandler();
    } else if (RequiresUser.class.equals(t)) {
      return new UserAnnotationHandler();
    } else if (RequiresGuest.class.equals(t)) {
      return new GuestAnnotationHandler();
    } else if (RequiresAuthentication.class.equals(t)) {
      return new AuthenticatedAnnotationHandler();
    }
    else throw new IllegalArgumentException("Cannot create a handler for the unknown for annotation " + t);
  }

  public ShiroAuthorizationFilter(ShiroAuthorizationService authorizationService,
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
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    this.authorizationService.invokeFilterChain(requestContext, responseContext);

    ExecContextProvider execContextProvider = (ExecContextProvider)requestContext
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContextProvider == null) {
      log.warn("Missing {} in {}!", ExecContextProvider.class.getSimpleName(), ContainerRequestContext.class.getSimpleName());
      return;
    }

    this.authorizationService.doAuthorizationChecks(requestContext, responseContext, authzChecks);

    ExecContext<?> execContext = execContextProvider.produce();
    if (execContext instanceof SecurityExecContext securityExecContext) {
      this.authorizationService.extractRequiredPermissionsToExecContext(securityExecContext, authzChecks);
    }
    Subject subject = this.authorizationService.subject();
    if (subject == null) {
      log.warn("Shiro Subject is missing!");
    } else {
      Object p = subject.getPrincipal();
      if (p instanceof Dto principal) {
        MDC.put(MDC_PERSON_ID, principal.identifier());
      }
      if (p instanceof User<?> principal) {
        MDC.put(MDC_PERSON_NAME, principal.getFirstName() + " " + principal.getLastName());
      }
    }
  }
}
