package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import com.dropchop.recyclone.mapper.api.ToEntityMapper;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 05. 24.
 */
public interface MapperProvider<D extends Dto, E extends Entity> {

  ToDtoMapper<D, E> getToDtoMapper();

  ToEntityMapper<D, E> getToEntityMapper();
}
