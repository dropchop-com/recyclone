package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.invoke.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
public class EntityDelegateFactory<D extends Dto, E extends Entity, ID>
  extends EntityLoadDelegate<D, E, ID> implements EntityFactoryListener<D, E> {

  public EntityDelegateFactory(EntityByIdService<D, E, ID> service) {
    super(service);
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
