package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.marker.HasName;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.AfterToEntityListener;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
public class SetName implements AfterToEntityListener {

  final Class<?> onlyForEntity;

  public SetName() {
    this.onlyForEntity = null;
  }

  public SetName( Class<?> onlyForEntity) {
    this.onlyForEntity = onlyForEntity;
  }

  @Override
  public void after(Dto dto, Entity entity, MappingContext context) {
    if (onlyForEntity != null && !entity.getClass().isAssignableFrom(onlyForEntity)) {
      return;
    }
    if (Constants.Actions.CREATE.equals(context.getSecurityAction())) { // for other actions it has no sense
      if (!(entity instanceof HasName)) {
        return;
      }
      String name = ((HasName) dto).getName();
      if (name == null) {
        return;
      }
      ((HasName) entity).setName(name);
    }
  }
}
