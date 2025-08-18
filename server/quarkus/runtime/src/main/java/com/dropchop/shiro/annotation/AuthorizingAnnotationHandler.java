package com.dropchop.shiro.annotation;

import org.apache.shiro.aop.AnnotationHandler;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * An AnnotationHandler that executes authorization (access control) behavior based on directive(s) found in a
 * JSR-175 Annotation.
 *
 * @see org.apache.shiro.authz.aop.AuthorizingAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 2025
 */
public abstract class AuthorizingAnnotationHandler extends AnnotationHandler {

  /**
   * Constructs an <code>AuthorizingAnnotationHandler</code> who processes annotations of the
   * specified type.  Immediately calls <code>super(annotationClass)</code>.
   *
   * @param annotationClass the type of annotation this handler will process.
   */
  public AuthorizingAnnotationHandler(Class<? extends Annotation> annotationClass) {
    super(annotationClass);
  }

  /**
   * Ensures the calling Subject is authorized to execute based on the directive(s) found in the given
   * annotation.
   * <p/>
   * As this is an AnnotationMethodInterceptor, the implementations of this method typically inspect the annotation
   * and perform a corresponding authorization check based.
   *
   * @param a the <code>Annotation</code> to check for performing an authorization check.
   * @throws org.apache.shiro.authz.AuthorizationException if the class/instance/method is not allowed to proceed/execute.
   */
  public abstract void assertAuthorized(Annotation a, Subject s) throws AuthorizationException;
}
