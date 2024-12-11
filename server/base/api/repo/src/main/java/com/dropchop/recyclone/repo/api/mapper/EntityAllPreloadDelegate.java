package com.dropchop.recyclone.repo.api.mapper;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.repo.api.ReadRepository;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 22.
 */
public class EntityAllPreloadDelegate<D extends Dto, E extends Entity, ID>
  extends EntityLoadDelegate<D, E, ID> {

  private final Map<String, E> preloaded;

  public EntityAllPreloadDelegate(ReadRepository<E, ID> repository) {
    super(repository);
    preloaded = repository.find()
      .stream()
      .collect(Collectors.toMap(E::identifier, Function.identity()));
  }

  @Override
  protected E findById(D dto) {
    return preloaded.get(dto.identifier());
  }
}
