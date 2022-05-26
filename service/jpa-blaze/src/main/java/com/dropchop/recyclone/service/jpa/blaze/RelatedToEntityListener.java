package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.mapping.AfterToEntityListener;
import com.dropchop.recyclone.service.api.mapping.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 05. 22.
 */
public class RelatedToEntityListener<D extends Dto, E extends Entity, ID, P extends Params>
  implements AfterToEntityListener<P> {

  private final EntityByIdService<D, E, ID> idService;

  public RelatedToEntityListener(EntityByIdService<D, E, ID> idService) {
    this.idService = idService;
  }

  @Override
  public void after(Dto dto, Entity entity, MappingContext<P> context) {

  }
}
