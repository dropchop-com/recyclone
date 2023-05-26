package com.dropchop.recyclone.model.api.security.annotations;

import java.lang.annotation.*;

/**
 * Modeled after Shiro annotations: org.apache.shiro.authz.annotation.RequiresRoles
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 23. 05. 23.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
