package com.dropchop.shiro.annotation;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * @see org.apache.shiro.authz.aop.AuthenticatedAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 2025
 */
public class AuthenticatedAnnotationHandler extends AuthorizingAnnotationHandler {

  /**
   * Default no-argument constructor that ensures this handler to process
   * {@link org.apache.shiro.authz.annotation.RequiresAuthentication RequiresAuthentication} annotations.
   */
  public AuthenticatedAnnotationHandler() {
    super(RequiresAuthentication.class);
  }

  /**
   * Ensures that the calling <code>Subject</code> is authenticated, and if not, throws an
   * {@link org.apache.shiro.authz.UnauthenticatedException UnauthenticatedException}
   * indicating the method is not allowed to be executed.
   *
   * @param a the annotation to inspect
   * @throws org.apache.shiro.authz.UnauthenticatedException if the calling <code>Subject</code> has not yet
   *                                                         authenticated.
   */
  public void assertAuthorized(Annotation a, Subject s) throws UnauthenticatedException {
    if (a instanceof RequiresAuthentication && !s.isAuthenticated()) {
      throw new UnauthenticatedException("The current Subject is not authenticated.  Access denied.");
    }
  }
}
