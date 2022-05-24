package com.dropchop.shiro.jaxrs;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.shiro.filter.AccessControlFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.authz.aop.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

import static com.dropchop.recyclone.model.api.invoke.ErrorCode.authentication_error;
import static com.dropchop.recyclone.model.api.invoke.ErrorCode.authorization_error;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
public class ShiroContainerFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private final Map<AuthorizingAnnotationHandler, Annotation> authzChecks;
  private final org.apache.shiro.mgt.SecurityManager securityManager;
  private final List<AccessControlFilter> accessControlFilters;

  private static AuthorizingAnnotationHandler createHandler(Annotation annotation) {
    Class<?> t = annotation.annotationType();
    if (RequiresPermissions.class.equals(t)) return new PermissionAnnotationHandler();
    else if (RequiresRoles.class.equals(t)) return new RoleAnnotationHandler();
    else if (RequiresUser.class.equals(t)) return new UserAnnotationHandler();
    else if (RequiresGuest.class.equals(t)) return new GuestAnnotationHandler();
    else if (RequiresAuthentication.class.equals(t)) return new AuthenticatedAnnotationHandler();
    else throw new IllegalArgumentException("Cannot create a handler for the unknown for annotation " + t);
  }

  public ShiroContainerFilter(List<AccessControlFilter> accessControlFilters,
                              org.apache.shiro.mgt.SecurityManager securityManager,
                              Collection<Annotation> authzSpecs) {
    Map<AuthorizingAnnotationHandler, Annotation> authChecks = new HashMap<>(authzSpecs.size());
    for (Annotation authSpec : authzSpecs) {
      authChecks.put(createHandler(authSpec), authSpec);
    }
    this.securityManager = securityManager;
    this.authzChecks = Collections.unmodifiableMap(authChecks);
    this.accessControlFilters = accessControlFilters;
    log.trace("Constructed {}", this.getClass().getName());
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    Subject subject = new Subject.Builder(securityManager).buildSubject();
    ThreadState threadState = new SubjectThreadState(subject);
    threadState.bind();
    requestContext.setProperty("shiro.req.internal.thread.state", threadState);
    requestContext.setSecurityContext(new ShiroSecurityContext(requestContext));
    try {
      for (AccessControlFilter filter: accessControlFilters) {
        boolean proceed = filter.isAccessAllowed(requestContext) || filter.onAccessDenied(requestContext);
        if (!proceed) {
          break;
        }
      }
      String[] requiredPermissions = null;
      Logical requiredPermissionsOp = Logical.AND;
      for (Map.Entry<AuthorizingAnnotationHandler, Annotation> authzCheck : authzChecks.entrySet()) {
        AuthorizingAnnotationHandler handler = authzCheck.getKey();
        Annotation authzSpec = authzCheck.getValue();
        if (authzSpec instanceof RequiresPermissions) {
          requiredPermissions = ((RequiresPermissions) authzSpec).value();
          requiredPermissionsOp = ((RequiresPermissions) authzSpec).logical();
        }
        handler.assertAuthorized(authzSpec);
      }
      //CommonExecContext<Params, Dto> execContext = CommonExecContextConsumer.provider.get();
      //if (execContext != null) {
        //execContext.setSubject(subject);
        requestContext.setProperty(InternalContextVariables.RECYCLONE_SECURITY_SUBJECT, subject);
        if (requiredPermissions != null && requiredPermissions.length > 0) {
          String securityDomainAction = requiredPermissions[0];
          if (securityDomainAction != null && !securityDomainAction.isBlank()) {
            //execContext.setSecurityDomain(Constants.Permission.decomposeDomain(securityDomainAction));
            requestContext.setProperty(InternalContextVariables.RECYCLONE_SECURITY_DOMAIN,
              Constants.Permission.decomposeDomain(securityDomainAction));

            //execContext.setSecurityAction(Constants.Permission.decomposeAction(securityDomainAction));
            requestContext.setProperty(InternalContextVariables.RECYCLONE_SECURITY_ACTION,
              Constants.Permission.decomposeAction(securityDomainAction));
            //log.trace("{} {}", execContext.getSecurityDomain(), execContext.getSecurityAction());
            log.error("Registering required security domain {} and action {}",
              Constants.Permission.decomposeDomain(securityDomainAction),
              Constants.Permission.decomposeAction(securityDomainAction));
          }
          if (requiredPermissions.length > 1) {
            log.warn("Only first permission in @RequiresPermissions annotation is passed to CommonExecContext!");
          }
          requestContext.setProperty(InternalContextVariables.RECYCLONE_SECURITY_REQUIRED_PERM,
            Arrays.asList(requiredPermissions));
          requestContext.setProperty(InternalContextVariables.RECYCLONE_SECURITY_REQUIRED_PERM_OP,
            requiredPermissionsOp);
          //execContext.setRequiredPermissions(Arrays.asList(requiredPermissions));
          //execContext.setRequiredPermissionsOp(requiredPermissionsOp);
        }
      //}
    } catch (AuthorizationException e) {
      threadState.clear();
      if (e instanceof UnauthenticatedException) {
        throw new ServiceException(authentication_error, "User is unauthenticated.");
      } else {
        throw new ServiceException(authorization_error, "User is unauthorized.");
      }
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
    Object threadStateObj = requestContext.getProperty("shiro.req.internal.thread.state");
    if (threadStateObj instanceof ThreadState threadState) {
      threadState.clear();
    }
  }
}
