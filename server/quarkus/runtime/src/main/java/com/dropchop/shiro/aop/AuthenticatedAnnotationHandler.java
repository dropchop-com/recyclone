package com.dropchop.shiro.aop;

import com.dropchop.recyclone.model.api.security.annotations.RequiresAuthentication;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import java.lang.annotation.Annotation;


/**
 * Modeled after: org.apache.shiro.authz.aop.AuthenticatedAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 23
 */
public class AuthenticatedAnnotationHandler extends AuthorizingAnnotationHandler {

    /**
     * Default no-argument constructor that ensures this handler to process
     * {@link RequiresAuthentication RequiresAuthentication} annotations.
     */
    public AuthenticatedAnnotationHandler() {
        super(RequiresAuthentication.class);
    }

    /**
     * Ensures that the calling <code>Subject</code> is authenticated, and if not, throws an
     * {@link UnauthenticatedException UnauthenticatedException} indicating the method is not allowed to be executed.
     *
     * @param a the annotation to inspect
     * @throws UnauthenticatedException if the calling <code>Subject</code> has not yet
     * authenticated.
     */
    public void assertAuthorized(Annotation a) throws UnauthenticatedException {
        if (a instanceof RequiresAuthentication && !getSubject().isAuthenticated() ) {
            throw new UnauthenticatedException( "The current Subject is not authenticated.  Access denied." );
        }
    }
}
