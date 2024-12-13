package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 22.
 */
public interface EntityFactoryListener<D extends Dto, E extends Entity>
  extends FactoryMappingListener {
  Class<E> getEntityType();
  E create(D dto, MappingContext context);
}
