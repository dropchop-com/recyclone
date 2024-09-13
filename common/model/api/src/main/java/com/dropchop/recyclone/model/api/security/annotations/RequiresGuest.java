package com.dropchop.recyclone.model.api.security.annotations;

import java.lang.annotation.*;
/**
 * Modeled after Shiro annotations: org.apache.shiro.authz.annotation.RequiresGuest
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 05. 23.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresGuest {
}
