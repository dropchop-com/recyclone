package com.dropchop.recyclone.base.api.model.rest;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.CommonParams;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.Params;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 03. 22.
 */
@SuppressWarnings("unused")
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicExecContext {
  Class<? extends Params> value() default CommonParams.class;
  Class<? extends Dto> dataClass() default Dto.class;
  Class<? extends ExecContext> execContextClass() default CommonExecContext.class;
  boolean internal() default false;
}
