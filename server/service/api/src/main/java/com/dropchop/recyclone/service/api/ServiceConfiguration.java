package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.mapper.api.ToDtoMapper;
import com.dropchop.recyclone.mapper.api.ToEntityMapper;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 2. 05. 22.
 */
public class ServiceConfiguration<D extends Dto, E extends Entity, ID> {

  final CrudRepository<E, ID> repository;
  final ToDtoMapper<D, E> toDtoMapper;
  final ToEntityMapper<D, E> toEntityMapper;

  public ServiceConfiguration(CrudRepository<E, ID> repository,
                              ToDtoMapper<D, E> toDtoMapper,
                              ToEntityMapper<D, E> toEntityMapper) {
    this.repository = repository;
    this.toDtoMapper = toDtoMapper;
    this.toEntityMapper = toEntityMapper;
  }

  public CrudRepository<E, ID> getRepository() {
    return repository;
  }

  public ToDtoMapper<D, E> getToDtoMapper() {
    return toDtoMapper;
  }

  public ToEntityMapper<D, E> getToEntityMapper() {
    return toEntityMapper;
  }
}
