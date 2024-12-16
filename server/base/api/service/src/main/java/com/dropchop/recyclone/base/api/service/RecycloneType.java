package com.dropchop.recyclone.base.api.service;

import jakarta.inject.Qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 06. 24.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
public @interface RecycloneType {

  /**
   * Qualifier which supports by name selection.
   *
   * @return the name.
   */
  String value() default "";
}
