package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.service.api.invoke.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public class AfterSetDeactivatedListener<P extends Params> extends RestrictedAfterToEntityListener<P> {

  public AfterSetDeactivatedListener() {
  }

  public AfterSetDeactivatedListener(Class<?> onlyForEntity) {
    super(onlyForEntity);
  }

  @Override
  public void after(Dto dto, Entity entity, MappingContext<P> context) {
    if (onlyForEntity != null && !entity.getClass().isAssignableFrom(onlyForEntity)) {
      return;
    }
    if (Actions.CREATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasDeactivated) {
        ((HasDeactivated) entity).setDeactivated(null);
      }
    } else if (Actions.UPDATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasDeactivated && dto instanceof HasDeactivated) {
        ((HasDeactivated) entity).setDeactivated(((HasDeactivated) dto).getDeactivated());
      }
    }
  }
}
