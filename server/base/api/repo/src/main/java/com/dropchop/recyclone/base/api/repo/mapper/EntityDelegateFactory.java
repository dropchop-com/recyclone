package com.dropchop.recyclone.base.api.repo.mapper;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.repo.ReadRepository;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
public class EntityDelegateFactory<D extends Dto, E extends Entity, ID>
  extends EntityLoadDelegate<D, E, ID> implements EntityFactoryListener<D, E> {

  public EntityDelegateFactory(ReadRepository<E, ID> repository) {
    super(repository);
  }

  @Override
  public EntityDelegateFactory<D, E, ID> forActionOnly(String action) {
    super.forActionOnly(action);
    return this;
  }

  @Override
  public EntityDelegateFactory<D, E, ID> failIfMissing(boolean failIfMissing) {
    super.failIfMissing(failIfMissing);
    return this;
  }

  @Override
  public EntityDelegateFactory<D, E, ID> failIfPresent(boolean failIfPresent) {
    super.failIfPresent(failIfPresent);
    return this;
  }

  @Override
  public E create(D dto, MappingContext context) {
    return this.load(dto, context);
  }
}
