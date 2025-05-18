package com.dropchop.recyclone.base.api.repo.mapper;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.repo.ReadRepository;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
public class EntityLoadDelegateFactory<D extends Dto, E extends Entity, ID>
  extends EntityLoadDelegate<D, E, ID> implements EntityFactoryListener<D, E> {

  public EntityLoadDelegateFactory(ReadRepository<E, ID> repository) {
    super(repository);
  }

  public <X extends Entity> EntityLoadDelegateFactory(ReadRepository<E, ID> repository, Collection<Class<X>> supported) {
    super(repository, supported);
  }

  @Override
  public EntityLoadDelegateFactory<D, E, ID> forActionOnly(String action) {
    super.forActionOnly(action);
    return this;
  }

  @Override
  public EntityLoadDelegateFactory<D, E, ID> failIfMissing(boolean failIfMissing) {
    super.failIfMissing(failIfMissing);
    return this;
  }

  @Override
  public EntityLoadDelegateFactory<D, E, ID> failIfPresent(boolean failIfPresent) {
    super.failIfPresent(failIfPresent);
    return this;
  }

  @Override
  public E create(D dto, MappingContext context) {
    return this.load(dto, context);
  }
}
