package com.dropchop.recyclone.base.api.repo;

import com.dropchop.recyclone.base.api.mapper.ToDtoMapper;
import com.dropchop.recyclone.base.api.mapper.ToEntityMapper;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 05. 24.
 */
public interface MapperProvider<D extends Dto, E extends Entity> {

  ToDtoMapper<D, E> getToDtoMapper();

  ToEntityMapper<D, E> getToEntityMapper();
}
