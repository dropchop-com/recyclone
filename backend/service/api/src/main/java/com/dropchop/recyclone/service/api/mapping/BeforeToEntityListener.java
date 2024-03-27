package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.service.api.invoke.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public interface BeforeToEntityListener extends BeforeMappingListener {
  void before(Model model, Entity entity, MappingContext context);
}
