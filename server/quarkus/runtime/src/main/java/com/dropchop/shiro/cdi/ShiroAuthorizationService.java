package com.dropchop.shiro.cdi;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.SecurityExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.service.security.AuthorizationService;
import com.dropchop.shiro.filter.RequestFilter;
import com.dropchop.shiro.filter.ResponseFilter;
import com.dropchop.shiro.filter.ShiroFilter;
import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

import static com.dropchop.recyclone.base.api.model.invoke.ErrorCode.authentication_error;
import static com.dropchop.recyclone.base.api.model.invoke.ErrorCode.authorization_error;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 06. 22.
 */
@DefaultBean
@ApplicationScoped
public class ShiroAuthorizationService extends ShiroAuthenticationService implements AuthorizationService {

  private static final Logger log = LoggerFactory.getLogger(ShiroAuthorizationService.class);

  @Inject
  Instance<ShiroFilter> allShiroFilters;

  @Inject
  ShiroEnabledFilters shiroEnabledFilters;

  public void invokeRequestFilterChain(ContainerRequestContext requestContext) {
    try {
      for (Class<? extends ShiroFilter> clazz : shiroEnabledFilters) {
        ShiroFilter filter = allShiroFilters.select(clazz).get();
        if (filter instanceof RequestFilter requestFilter) {
          boolean proceed = requestFilter.onFilterRequest(requestContext);
          if (!proceed) {
            break;
          }
        }
      }
    } catch (AuthorizationException e) {
      unbindSubject(requestContext);
      if (e instanceof UnauthenticatedException) {
        throw new ServiceException(authentication_error, "User is unauthenticated.");
      } else {
        throw new ServiceException(authorization_error, "User is unauthorized.");
      }
    }
  }

  public void invokeResponseFilterChain(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    try {
      for (Class<? extends ShiroFilter> clazz : shiroEnabledFilters) {
        ShiroFilter filter = allShiroFilters.select(clazz).get();
        if (filter instanceof ResponseFilter responseFilter) {
          boolean proceed = responseFilter.onFilterResponse(requestContext, responseContext);
          if (!proceed) {
            break;
          }
        }
      }
    } catch (AuthorizationException e) {
      unbindSubject(requestContext);
      if (e instanceof UnauthenticatedException) {
        throw new ServiceException(authentication_error, "User is unauthenticated.");
      } else {
        throw new ServiceException(authorization_error, "User is unauthorized.");
      }
    }
  }

  public void doAuthorizationChecks(ContainerRequestContext requestContext,
                                    Map<AuthorizingAnnotationHandler, Annotation> authzChecks) {
    for (Map.Entry<AuthorizingAnnotationHandler, Annotation> authzCheck : authzChecks.entrySet()) {
      AuthorizingAnnotationHandler handler = authzCheck.getKey();
      Annotation authzSpec = authzCheck.getValue();
      try {
        handler.assertAuthorized(authzSpec);
      } catch (AuthorizationException e) {
        unbindSubject(requestContext);
        if (e instanceof UnauthenticatedException) {
          throw new ServiceException(authentication_error, "User is unauthenticated.");
        } else {
          throw new ServiceException(authorization_error, "User is unauthorized.");
        }
      }
    }
  }

  public void extractRequiredPermissionsToExecContext(SecurityExecContext execContext,
                                                      Map<AuthorizingAnnotationHandler, Annotation> authzChecks) {
    String[] requiredPermissions = null;
    Logical requiredPermissionsOp = Logical.AND;
    for (Map.Entry<AuthorizingAnnotationHandler, Annotation> authzCheck : authzChecks.entrySet()) {
      Annotation authzSpec = authzCheck.getValue();
      if (authzSpec instanceof RequiresPermissions) {
        requiredPermissions = ((RequiresPermissions) authzSpec).value();
        requiredPermissionsOp = ((RequiresPermissions) authzSpec).logical();
      }
    }

    if (requiredPermissions != null && requiredPermissions.length > 0) {
      execContext.setRequiredPermissions(Arrays.asList(requiredPermissions));
      execContext.setRequiredPermissionsOp(
          requiredPermissionsOp == Logical.AND ?
              com.dropchop.recyclone.base.api.model.security.annotations.Logical.AND :
              com.dropchop.recyclone.base.api.model.security.annotations.Logical.OR
      );
    }
  }

  public boolean isPermitted(Iterable<String> permissions, Logical op) {
    if (op == null) {
      op = Logical.AND;
    }
    Subject subject = SecurityUtils.getSubject();
    if (subject == null) {
      log.warn("Shiro Subject is missing!");
      return false;
    }
    for (String permission : permissions) {
      if (Logical.AND.equals(op)) {
        if (!subject.isPermitted(permission)) {
          return false;
        }
      } else if (Logical.OR.equals(op)) {
        if (subject.isPermitted(permission)) {
          return true;
        }
      }
    }
    return Logical.AND.equals(op);
  }

  @Override
  public boolean isPermitted(String permission) {
    return this.isPermitted(Collections.singleton(permission), Logical.AND);
  }

  @Override
  public boolean isPermitted(String domain, String action) {
    return this.isPermitted(Constants.Permission.compose(domain, action));
  }

  @Override
  public <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String domain, String action) {
    throw new UnsupportedOperationException("not yet implemented");
  }

  @Override
  public <M extends Model> boolean isPermitted(Class<M> subject, String identifier, String permission) {
    throw new UnsupportedOperationException("not yet implemented");
  }
}
