package com.dropchop.recyclone.base.api.model.security.annotations;

import java.lang.annotation.*;

/**
 * Modeled after Shiro annotations: org.apache.shiro.authz.annotation.RequiresRoles
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 05. 23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@SuppressWarnings("unused")
public @interface RequiresRoles {

    /**
     * A single String role name or multiple comma-delimited role names required in order for the method
     * invocation to be allowed.
     */
    String[] value();

    /**
     * The logical operation for the permission check in case multiple roles are specified. AND is the default
     * @since 1.1.0
     */
    Logical logical() default Logical.AND;
}
