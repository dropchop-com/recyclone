package com.dropchop.shiro.cdi;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.security.AccessKey;
import com.dropchop.recyclone.base.api.service.security.AuthenticationService;
import com.dropchop.recyclone.base.api.service.security.ClientAccessKeyService;
import com.dropchop.shiro.jaxrs.ShiroSecurityContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

public class ShiroAuthenticationService implements AuthenticationService {

  private static final Logger log = LoggerFactory.getLogger(ShiroAuthenticationService.class);
  public static final String SHIRO_REQ_INTERNAL_THREAD_STATE = "shiro.req.internal.thread.state";
  public static final String SHIRO_REQ_INTERNAL_SERVICE = "shiro.req.internal.service";

  /*@Inject
  SecurityManager shiroSecurityManager;

  @Produces
  @RequestScoped
  public Subject getSubject() {
    return SecurityUtils.getSubject();
  }*/

  @Inject
  Subject subject;

  @Inject
  @RecycloneType(RECYCLONE_DEFAULT)
  @SuppressWarnings({"CdiInjectionPointsInspection", "RedundantSuppression"})
  ClientAccessKeyService clientAccessKeyService;

  public Subject getSubject() {
    return subject;
  }

  /*private ThreadState bindSubject() {
    ThreadContext.bind(shiroSecurityManager);
    Subject subject = new Subject.Builder(shiroSecurityManager).buildSubject();
    ThreadContext.bind(subject);
    ThreadState threadState = new SubjectThreadState(subject);
    threadState.bind();
    return threadState;
  }*/

  public void bindSubject(ContainerRequestContext requestContext) {
    //ThreadState threadState = this.bindSubject();
    //requestContext.setProperty(SHIRO_REQ_INTERNAL_THREAD_STATE, threadState);
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
      Object principal = subject.getPrincipal();
      if (principal instanceof HasId hasId) {
        Map<AccessKey, String> keys = clientAccessKeyService.createAccessKeys(hasId, token);
        if (principal instanceof HasAttributes hasAttributes) {
          for (Map.Entry<AccessKey, String> entry : keys.entrySet()) {
            AccessKey key = entry.getKey();
            hasAttributes.setAttributeValue(key.toString(), entry.getValue());
          }
        }
      }
      return subject;
    } catch (AuthenticationException e) {
      log.warn("Authentication failed for token [{}]", token, e);
      throw e;
    }
  }
}
