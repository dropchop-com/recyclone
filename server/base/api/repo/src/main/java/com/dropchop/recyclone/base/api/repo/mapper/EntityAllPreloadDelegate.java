package com.dropchop.recyclone.base.api.repo.mapper;

import com.dropchop.recyclone.base.api.mapper.FilteringDtoContext;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.repo.ReadRepository;
import com.dropchop.recyclone.base.dto.model.invoke.Params;

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
    MappingContext mapContext = new FilteringDtoContext();
    Params params = new Params();
    params.getFilter().setSize(1000);
    mapContext.setParams(params);
    preloaded = repository.find(repository.getRepositoryExecContext(mapContext))
      .stream()
      .collect(Collectors.toMap(E::identifier, Function.identity()));
  }

  @Override
  protected E findById(D dto) {
    return preloaded.get(dto.identifier());
  }
}
