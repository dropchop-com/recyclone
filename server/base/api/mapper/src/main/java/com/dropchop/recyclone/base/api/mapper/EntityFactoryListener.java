package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 05. 22.
 */
public interface EntityFactoryListener<D extends Dto, E extends Entity>
  extends FactoryMappingListener {
  <X extends E> boolean supports(Class<X> xClass);
  E create(D dto, MappingContext context);
}
