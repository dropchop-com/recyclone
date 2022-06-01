package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.repo.api.CrudRepository;
import com.dropchop.recyclone.repo.jpa.blaze.*;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;
import com.dropchop.recyclone.service.api.mapping.ToDtoMapper;
import com.dropchop.recyclone.service.api.mapping.ToEntityMapper;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 2. 05. 22.
 */
public class ServiceConfiguration<D extends Dto, P extends Params, E extends Entity, ID> {

  final CrudRepository<E, ID> repository;
  final ToDtoMapper<D, P, E> toDtoMapper;
  final ToEntityMapper<D, P, E> toEntityMapper;
  final CommonExecContext<P, D> execContext;
  final Iterable<BlazeCriteriaDecorator<E, P>> criteriaDecorators;

  public ServiceConfiguration(CrudRepository<E, ID> repository,
                              ToDtoMapper<D, P, E> toDtoMapper,
                              ToEntityMapper<D, P, E> toEntityMapper,
                              CommonExecContext<P, D> execContext,
                              Iterable<BlazeCriteriaDecorator<E, P>> criteriaDecorators) {
    this.repository = repository;
    this.toDtoMapper = toDtoMapper;
    this.toEntityMapper = toEntityMapper;
    this.execContext = execContext;
    this.criteriaDecorators = criteriaDecorators;
  }

  public ServiceConfiguration(CrudRepository<E, ID> repository,
                              ToDtoMapper<D, P, E> toDtoMapper,
                              ToEntityMapper<D, P, E> toEntityMapper,
                              CommonExecContext<P, D> execContext) {
    this(repository, toDtoMapper, toEntityMapper, execContext, List.of(
      new LikeIdentifierCriteriaDecorator<>(),
      new InlinedStatesCriteriaDecorator<>(),
      new SortCriteriaDecorator<>(),
      new PageCriteriaDecorator<>()
    ));
  }

  public CrudRepository<E, ID> getRepository() {
    return repository;
  }

  public ToDtoMapper<D, P, E> getToDtoMapper() {
    return toDtoMapper;
  }

  public ToEntityMapper<D, P, E> getToEntityMapper() {
    return toEntityMapper;
  }

  public CommonExecContext<P, D> getExecContext() {
    return execContext;
  }

  public Iterable<BlazeCriteriaDecorator<E, P>> getCriteriaDecorators() {
    return criteriaDecorators;
  }
}
