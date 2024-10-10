package com.dropchop.shiro.jaxrs;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.authz.aop.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 12. 21.
 */
@RequestScoped
public class ShiroAuthorizationFilter2 implements ContainerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(ShiroAuthorizationFilter2.class);

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

  @Inject
  ShiroAuthorizationService authorizationService;

  @SuppressWarnings("unused")
  public ShiroAuthorizationFilter2() {
  }

  @Override
  public void filter(ContainerRequestContext context) throws IOException {
    ExecContext<?> execContext = (ExecContext<?>)context
        .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    log.debug("==================Invoked [{}]", context.getUriInfo().getPath());
    if (execContext == null) {
      log.warn(
          "Missing {} in {}!", ExecContext.class.getSimpleName(), ContainerRequestContext.class.getSimpleName()
      );
    }
  }
}
