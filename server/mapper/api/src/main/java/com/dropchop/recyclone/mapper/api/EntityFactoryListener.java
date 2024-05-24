package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.mapper.api.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 05. 22.
 */
public interface EntityFactoryListener<D extends Dto, E extends Entity>
  extends FactoryMappingListener {
  Class<E> getEntityType();
  E create(D dto, MappingContext context);
}
