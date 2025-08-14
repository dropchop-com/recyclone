package com.dropchop.shiro.annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * @see org.apache.shiro.authz.aop.GuestAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 2025
 */
public class GuestAnnotationHandler extends AuthorizingAnnotationHandler {

  /**
   * Default no-argument constructor that ensures this interceptor looks for
   * <p>
   * {@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest} annotations in a method
   * declaration.
   */
  public GuestAnnotationHandler() {
    super(RequiresGuest.class);
  }

  /**
   * Ensures that the calling <code>Subject</code> is NOT a <em>user</em>, that is, they do not
   * have an {@link org.apache.shiro.subject.Subject#getPrincipal() identity} before continuing.  If they are
   * a user ({@link org.apache.shiro.subject.Subject#getPrincipal() Subject.getPrincipal()} != null), an
   * <code>AuthorizingException</code> will be thrown indicating that execution is not allowed to continue.
   *
   * @param a the annotation to check for one or more roles
   * @throws org.apache.shiro.authz.AuthorizationException if the calling <code>Subject</code> is not a &quot;guest&quot;.
   */
  public void assertAuthorized(Annotation a, Subject s) throws AuthorizationException {
    if (a instanceof RequiresGuest && s.getPrincipal() != null) {
      throw new UnauthenticatedException("Attempting to perform a guest-only operation.  The current Subject is "
          + "not a guest (they have been authenticated or remembered from a previous login).  Access "
          + "denied.");
    }
  }
}
