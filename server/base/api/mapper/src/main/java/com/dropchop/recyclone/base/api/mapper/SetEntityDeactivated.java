package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.security.Constants.Actions;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
@SuppressWarnings("unused")
public class SetEntityDeactivated extends RestrictedAfterToEntityListener {

  public SetEntityDeactivated() {
  }

  public SetEntityDeactivated(Class<?> onlyForEntity) {
    super(onlyForEntity);
  }

  @Override
  public void after(Model model, Entity entity, MappingContext context) {
    if (onlyForEntity != null && !onlyForEntity.isAssignableFrom(entity.getClass())) {
      return;
    }
    if (Actions.CREATE.equals(context.getSecurityAction()) || Actions.UPDATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasDeactivated && model instanceof HasDeactivated) {
        ((HasDeactivated) entity).setDeactivated(((HasDeactivated) model).getDeactivated());
      }
    }
  }
}
