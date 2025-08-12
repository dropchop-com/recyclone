package com.dropchop.shiro.annotation;

import jakarta.annotation.security.PermitAll;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * @see org.apache.shiro.authz.aop.PermitAllAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 2025
 */
@SuppressWarnings("unused")
public class PermitAllAnnotationHandler extends AuthorizingAnnotationHandler {

  /**
   * Default no-argument constructor that ensures this interceptor looks for a {@link PermitAll}
   * annotation in a method declaration.
   */
  public PermitAllAnnotationHandler() {
    super(PermitAll.class);
  }

  /**
   * No-op, the {@link PermitAll} annotation allows all subjects (including guests/anonymous).
   *
   * @param a the annotation to check for one or more roles
   */
  @Override
  public void assertAuthorized(Annotation a, Subject s) {
  }
}
