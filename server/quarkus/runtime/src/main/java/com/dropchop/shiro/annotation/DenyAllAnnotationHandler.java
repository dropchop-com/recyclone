package com.dropchop.shiro.annotation;

import jakarta.annotation.security.DenyAll;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * @see org.apache.shiro.authz.aop.DenyAllAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 2025
 */
@SuppressWarnings("unused")
public class DenyAllAnnotationHandler extends AuthorizingAnnotationHandler {

  /**
   * Default no-argument constructor that ensures this interceptor looks for
   * <p>
   * {@link org.apache.shiro.authz.annotation.RequiresGuest RequiresGuest} annotations in a method
   * declaration.
   */
  public DenyAllAnnotationHandler() {
    super(DenyAll.class);
  }

  /**
   * Causes a {@link UnauthorizedException} to be thrown if a DenyAll annotation is present.
   *
   * @param a the annotation to check for one or more roles
   * @throws UnauthorizedException when the DenyAll annotation is present
   */
  public void assertAuthorized(Annotation a, Subject s) throws UnauthorizedException {
    throw new UnauthenticatedException("Attempting to perform a denied operation.");
  }
}
