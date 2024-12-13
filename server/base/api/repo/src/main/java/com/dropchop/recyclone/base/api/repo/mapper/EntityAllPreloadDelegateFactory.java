package com.dropchop.recyclone.base.api.repo.mapper;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.repo.ReadRepository;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
@SuppressWarnings("unused")
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
