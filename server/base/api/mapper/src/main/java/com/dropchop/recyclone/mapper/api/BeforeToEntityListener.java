package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.mapper.api.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
public interface BeforeToEntityListener extends BeforeMappingListener {
  void before(Model model, Entity entity, MappingContext context);
}
