package com.dropchop.recyclone.rest.jaxrs.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 03. 22.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicExecContext {
  Class<? extends Params> value() default Params.class;
  Class<? extends Dto> dataClass() default Dto.class;
}
