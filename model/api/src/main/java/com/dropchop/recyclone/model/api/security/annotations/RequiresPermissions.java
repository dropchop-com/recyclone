package com.dropchop.recyclone.model.api.security.annotations;

import java.lang.annotation.*;

/**
 * Modeled after Shiro annotations.
 * To be more exact: org.apache.shiro.authz.annotation.RequiresPermissions
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresPermissions {

    /**
     * The permission string to determine if the user is allowed to invoke the code protected by this annotation.
     */
    String[] value();
    
    /**
     * The logical operation for the permission checks in case multiple roles are specified. AND is the default
     * @since 1.1.0
     */
    Logical logical() default Logical.AND;
}
