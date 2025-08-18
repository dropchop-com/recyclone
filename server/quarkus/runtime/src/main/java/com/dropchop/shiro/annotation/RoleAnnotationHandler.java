package com.dropchop.shiro.annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @see org.apache.shiro.authz.aop.RoleAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 2025
 */
public class RoleAnnotationHandler extends AuthorizingAnnotationHandler {

  /**
   * Default no-argument constructor that ensures this handler looks for
   * {@link org.apache.shiro.authz.annotation.RequiresRoles RequiresRoles} annotations.
   */
  public RoleAnnotationHandler() {
    super(RequiresRoles.class);
  }

  /**
   * Ensures that the calling <code>Subject</code> has the Annotation's specified roles, and if not, throws an
   * <code>AuthorizingException</code> indicating that access is denied.
   *
   * @param a the RequiresRoles annotation to use to check for one or more roles
   * @throws org.apache.shiro.authz.AuthorizationException if the calling <code>Subject</code> does not have the role(s)
   *                                                       necessary to proceed.
   */
  public void assertAuthorized(Annotation a, Subject s) throws AuthorizationException {
    if (!(a instanceof RequiresRoles rrAnnotation)) {
      return;
    }

    String[] roles = rrAnnotation.value();

    if (roles.length == 1) {
      s.checkRole(roles[0]);
      return;
    }
    if (Logical.AND.equals(rrAnnotation.logical())) {
      s.checkRoles(Arrays.asList(roles));
      return;
    }
    if (Logical.OR.equals(rrAnnotation.logical())) {
      // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
      boolean hasAtLeastOneRole = false;
      for (String role : roles) {
        if (s.hasRole(role)) {
          hasAtLeastOneRole = true;
        }
      }
      // Cause the exception if none of the roles match, note that the exception message will be a bit misleading
      if (!hasAtLeastOneRole) {
        s.checkRole(roles[0]);
      }
    }
  }
}
