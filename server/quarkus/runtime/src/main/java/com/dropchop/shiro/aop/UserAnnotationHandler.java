package com.dropchop.shiro.aop;

import com.dropchop.recyclone.model.api.security.annotations.RequiresUser;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import java.lang.annotation.Annotation;


/**
 * Modeled after: org.apache.shiro.authz.aop.UserAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 23
 */
public class UserAnnotationHandler extends AuthorizingAnnotationHandler {

  /**
   * Default no-argument constructor that ensures this handler looks for
   * {@link RequiresUser RequiresUser} annotations.
   */
  public UserAnnotationHandler() {
    super(RequiresUser.class);
  }

  /**
   * Ensures that the calling <code>Subject</code> is a <em>user</em>, that is, they are <em>either</code>
   * {@link org.apache.shiro.subject.Subject#isAuthenticated() authenticated} <b><em>or</em></b> remembered via remember
   * me services before allowing access, and if not, throws an
   * <code>AuthorizingException</code> indicating access is not allowed.
   *
   * @param a the RequiresUser annotation to check
   * @throws AuthorizationException if the calling <code>Subject</code> is not authenticated or remembered via rememberMe services.
   */
  public void assertAuthorized(Annotation a) throws AuthorizationException {
    if (a instanceof RequiresUser && getSubject().getPrincipal() == null) {
      throw new UnauthenticatedException("Attempting to perform a user-only operation.  The current Subject is " +
        "not a user (they haven't been authenticated or remembered from a previous login).  " +
        "Access denied.");
    }
  }
}
