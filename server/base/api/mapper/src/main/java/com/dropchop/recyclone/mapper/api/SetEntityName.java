package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasName;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.mapper.api.MappingContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
public class SetEntityName implements AfterToEntityListener {

  final Class<?> onlyForEntity;

  public SetEntityName() {
    this.onlyForEntity = null;
  }

  public SetEntityName(Class<?> onlyForEntity) {
    this.onlyForEntity = onlyForEntity;
  }

  @Override
  public void after(Model dto, Entity entity, MappingContext context) {
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
