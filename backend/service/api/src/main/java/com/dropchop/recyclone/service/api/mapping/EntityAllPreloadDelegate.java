package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.service.api.EntityByIdService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 05. 22.
 */
public class EntityAllPreloadDelegate<D extends Dto, E extends Entity, ID>
  extends EntityLoadDelegate<D, E, ID> {

  private final Map<String, E> preloaded;

  public EntityAllPreloadDelegate(EntityByIdService<D, E, ID> service) {
    super(service);
    preloaded = service.find()
      .stream()
      .collect(Collectors.toMap(E::identifier, Function.identity()));
  }

  @Override
  protected E findById(D dto) {
    return preloaded.get(dto.identifier());
  }
}
