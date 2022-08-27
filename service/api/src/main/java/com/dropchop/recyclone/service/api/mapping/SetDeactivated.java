package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.service.api.invoke.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public class SetDeactivated extends RestrictedAfterToEntityListener {

  public SetDeactivated() {
  }

  public SetDeactivated(Class<?> onlyForEntity) {
    super(onlyForEntity);
  }

  @Override
  public void after(Model model, Entity entity, MappingContext context) {
    if (onlyForEntity != null && !onlyForEntity.isAssignableFrom(entity.getClass())) {
      return;
    }
    if (Actions.CREATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasDeactivated) {
        ((HasDeactivated) entity).setDeactivated(null);
      }
    } else if (Actions.UPDATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasDeactivated && model instanceof HasDeactivated) {
        ((HasDeactivated) entity).setDeactivated(((HasDeactivated) model).getDeactivated());
      }
    }
  }
}
