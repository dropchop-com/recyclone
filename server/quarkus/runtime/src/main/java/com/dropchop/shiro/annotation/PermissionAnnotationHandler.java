package com.dropchop.shiro.annotation;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * @see org.apache.shiro.authz.aop.PermissionAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 2025
 */
public class PermissionAnnotationHandler extends AuthorizingAnnotationHandler{

  /**
   * Default no-argument constructor that ensures this handler looks for
   * {@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions} annotations.
   */
  public PermissionAnnotationHandler() {
    super(RequiresPermissions.class);
  }

  /**
   * Returns the annotation {@link RequiresPermissions#value value}, from which the Permission will be constructed.
   *
   * @param a the RequiresPermissions annotation being inspected.
   * @return the annotation's <code>value</code>, from which the Permission will be constructed.
   */
  protected String[] getAnnotationValue(Annotation a) {
    RequiresPermissions rpAnnotation = (RequiresPermissions) a;
    return rpAnnotation.value();
  }

  /**
   * Ensures that the calling <code>Subject</code> has the Annotation's specified permissions, and if not, throws an
   * <code>AuthorizingException</code> indicating access is denied.
   *
   * @param a the RequiresPermission annotation being inspected to check for one or more permissions
   * @throws org.apache.shiro.authz.AuthorizationException if the calling <code>Subject</code> does not have
   *                                                       the permission(s) necessary to continue access or execution.
   */
  public void assertAuthorized(Annotation a, Subject subject) throws AuthorizationException {
    if (!(a instanceof RequiresPermissions rpAnnotation)) {
      return;
    }

    String[] perms = getAnnotationValue(a);

    if (perms.length == 1) {
      subject.checkPermission(perms[0]);
      return;
    }
    if (Logical.AND.equals(rpAnnotation.logical())) {
      getSubject().checkPermissions(perms);
      return;
    }
    if (Logical.OR.equals(rpAnnotation.logical())) {
      // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
      boolean hasAtLeastOnePermission = false;
      for (String permission : perms) {
        if (getSubject().isPermitted(permission)) {
          hasAtLeastOnePermission = true;
          break;
        }
      }
      // Cause the exception if none of the roles match, note that the exception message will be a bit misleading
      if (!hasAtLeastOnePermission) {
        getSubject().checkPermission(perms[0]);
      }
    }
  }
}
