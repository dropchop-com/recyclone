package com.dropchop.shiro.aop;

import com.dropchop.recyclone.model.api.security.annotations.Logical;
import com.dropchop.recyclone.model.api.security.annotations.RequiresRoles;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Modeled after: org.apache.shiro.authz.aop.RoleAnnotationHandler
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 05. 23
 */
public class RoleAnnotationHandler extends AuthorizingAnnotationHandler {

    /**
     * Default no-argument constructor that ensures this handler looks for
     * {@link RequiresRoles RequiresRoles} annotations.
     */
    public RoleAnnotationHandler() {
        super(RequiresRoles.class);
    }

    /**
     * Ensures that the calling <code>Subject</code> has the Annotation's specified roles, and if not, throws an
     * <code>AuthorizingException</code> indicating that access is denied.
     *
     * @param a the RequiresRoles annotation to use to check for one or more roles
     * @throws AuthorizationException
     *          if the calling <code>Subject</code> does not have the role(s) necessary to
     *          proceed.
     */
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        if (!(a instanceof RequiresRoles)) return;

        RequiresRoles rrAnnotation = (RequiresRoles) a;
        String[] roles = rrAnnotation.value();

        if (roles.length == 1) {
            getSubject().checkRole(roles[0]);
            return;
        }
        if (Logical.AND.equals(rrAnnotation.logical())) {
            getSubject().checkRoles(Arrays.asList(roles));
            return;
        }
        if (Logical.OR.equals(rrAnnotation.logical())) {
            // Avoid processing exceptions unnecessarily - "delay" throwing the exception by calling hasRole first
            boolean hasAtLeastOneRole = false;
            for (String role : roles) if (getSubject().hasRole(role)) hasAtLeastOneRole = true;
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            if (!hasAtLeastOneRole) getSubject().checkRole(roles[0]);
        }
    }

}
