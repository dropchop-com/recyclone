package com.dropchop.shiro.jaxrs;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.SecurityExecContext;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.authz.aop.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_PERSON_ID;
import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_PERSON_NAME;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 12. 21.
 */
public class ShiroAuthorizationFilter implements ContainerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(ShiroAuthorizationFilter.class);

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

  @SuppressWarnings("unused")
  public ShiroAuthorizationFilter(ShiroAuthorizationService authorizationService,
                                  Collection<Annotation> authzSpecs) {
    Map<AuthorizingAnnotationHandler, Annotation> authChecks = new HashMap<>(authzSpecs.size());
    for (Annotation authSpec : authzSpecs) {
      authChecks.put(createHandler(authSpec), authSpec);
    }
    this.authorizationService = authorizationService;
    this.authzChecks = Collections.unmodifiableMap(authChecks);
  }

  @Override
  public void filter(ContainerRequestContext context) throws IOException {
    this.authorizationService.invokeRequestFilterChain(context);

    ExecContext<?> execContext = (ExecContext<?>)context
        .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContext == null) {
      log.warn(
          "Missing {} in {}!", ExecContext.class.getSimpleName(), ContainerRequestContext.class.getSimpleName()
      );
      return;
    }

    this.authorizationService.doAuthorizationChecks(context, authzChecks);
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
      if (p instanceof User principal) {
        MDC.put(MDC_PERSON_NAME, principal.getFirstName() + " " + principal.getLastName());
      }
    }
  }
}
