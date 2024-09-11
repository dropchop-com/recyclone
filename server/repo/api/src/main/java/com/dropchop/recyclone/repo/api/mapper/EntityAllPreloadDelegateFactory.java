package com.dropchop.recyclone.repo.api.mapper;

import com.dropchop.recyclone.mapper.api.EntityFactoryListener;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.repo.api.ReadRepository;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
public class EntityAllPreloadDelegateFactory<D extends Dto, E extends Entity, ID>
  extends EntityAllPreloadDelegate<D, E, ID>
  implements EntityFactoryListener<D, E> {

  public EntityAllPreloadDelegateFactory(ReadRepository<E, ID> repository) {
    super(repository);
  }

  @Override
  public EntityAllPreloadDelegateFactory<D, E, ID> forActionOnly(String action) {
    super.forActionOnly(action);
    return this;
  }

  @Override
  public EntityAllPreloadDelegateFactory<D, E, ID> failIfMissing(boolean failIfMissing) {
    super.failIfMissing(failIfMissing);
    return this;
  }

  @Override
  public EntityAllPreloadDelegateFactory<D, E, ID> failIfPresent(boolean failIfPresent) {
    super.failIfPresent(failIfPresent);
    return this;
  }

  @Override
  public E create(D dto, MappingContext context) {
    return this.load(dto, context);
  }
}
