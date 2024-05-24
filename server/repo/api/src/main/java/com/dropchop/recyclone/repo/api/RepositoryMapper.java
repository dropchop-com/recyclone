package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import com.dropchop.recyclone.mapper.api.ToEntityMapper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 24. 05. 24.
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface RepositoryMapper {
  Class<? extends ToDtoMapper<?, ?>> toDto();
  Class<? extends ToEntityMapper<?, ?>> toEntity();
}
