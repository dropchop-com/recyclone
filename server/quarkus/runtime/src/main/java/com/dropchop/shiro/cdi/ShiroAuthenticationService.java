package com.dropchop.shiro.cdi;

import com.dropchop.recyclone.base.api.model.security.PermissionBearer;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.shiro.jaxrs.ShiroSecurityContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroAuthenticationService implements AuthenticationService {

  private static final Logger log = LoggerFactory.getLogger(ShiroAuthenticationService.class);
  public static final String SHIRO_REQ_INTERNAL_THREAD_STATE = "shiro.req.internal.thread.state";
  public static final String SHIRO_REQ_INTERNAL_SERVICE = "shiro.req.internal.service";

  @Inject
  SecurityManager shiroSecurityManager;

  @Produces
  @RequestScoped
  public Subject getSubject() {
    return SecurityUtils.getSubject();
  }

  @Produces
  @RequestScoped
  public PermissionBearer getPermissionBearer() {
    try {
      Subject subject = SecurityUtils.getSubject();
      Object principal = subject.getPrincipal();
      if (principal == null) {
        log.warn("Shiro Subject is missing its principal!");
        return null;
      }
      if (principal instanceof PermissionBearer permissionBearer) {
        return permissionBearer;
      }
      log.warn("Shiro Subject principal is not of type [{}]!", PermissionBearer.class);
      return null;
    } catch (UnavailableSecurityManagerException e) {
      log.debug("Security manager not available!");
      return null;
    }
  }

  public ThreadState bindSubject() {
    Subject subject = new Subject.Builder(shiroSecurityManager).buildSubject();
    ThreadState threadState = new SubjectThreadState(subject);
    threadState.bind();
    return threadState;
  }

  public void bindSubject(ContainerRequestContext requestContext) {
    ThreadState threadState = this.bindSubject();
    requestContext.setProperty(SHIRO_REQ_INTERNAL_THREAD_STATE, threadState);
    requestContext.setProperty(SHIRO_REQ_INTERNAL_SERVICE, this);
    requestContext.setSecurityContext(new ShiroSecurityContext(requestContext));
  }

  public void unbindSubject(ContainerRequestContext requestContext) {
    Object threadStateObj = requestContext.getProperty(SHIRO_REQ_INTERNAL_THREAD_STATE);
    if (threadStateObj instanceof ThreadState threadState) {
      threadState.clear();
    }
  }

  public Subject login(AuthenticationToken token) {
    if (token == null) {
      String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
          "must be created in order to execute a login attempt.";
      throw new IllegalStateException(msg);
    }
    try {
      Subject subject = getSubject();
      subject.login(token);
      return subject;
    } catch (AuthenticationException e) {
      log.warn("Authentication failed for token [{}]", token, e);
      throw e;
    }
  }
}
