package com.dropchop.recyclone.base.api.model.security.annotations;

import java.lang.annotation.*;

/**
 * Modeled after Shiro annotations: org.apache.shiro.authz.annotation.RequiresUser
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 05. 23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@SuppressWarnings("unused")
public @interface RequiresUser {
}
