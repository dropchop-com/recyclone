package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.localization.Translation;
import com.dropchop.recyclone.model.api.marker.HasTranslation;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.service.api.invoke.MappingContext;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public class SetModification
  extends RestrictedAfterToEntityListener {

  public SetModification() {
  }

  public SetModification(Class<?> onlyForEntity) {
    super(onlyForEntity);
  }

  @Override
  public void after(Dto dto, Entity entity, MappingContext context) {
    if (onlyForEntity != null && !entity.getClass().isAssignableFrom(onlyForEntity)) {
      return;
    }
    boolean isCreateOrUpdate = Actions.CREATE.equals(context.getSecurityAction()) || Actions.UPDATE.equals(context.getSecurityAction());
    if (isCreateOrUpdate) {
      if (entity instanceof HasModified && ((HasModified) entity).getModified() == null) {
        ((HasModified) entity).setModified(ZonedDateTime.now());
      }
      if (entity instanceof HasTranslation<?> && dto instanceof HasTranslation<?>) {
        Set<? extends Translation> dtoTranslations = ((HasTranslation<?>) dto).getTranslations();
        if (dtoTranslations != null) {
          for (Translation t : dtoTranslations) {
            Translation entityTrans = ((HasTranslation<?>) entity).getTranslation(t.getLang());
            if (entityTrans instanceof HasCreated) {
              ((HasCreated) entityTrans).setCreated(ZonedDateTime.now());
            }
            if (entityTrans instanceof HasModified) {
              ((HasModified) entityTrans).setModified(ZonedDateTime.now());
            }
          }
        }
      }
    }
    if (Actions.CREATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasCreated && ((HasCreated) entity).getCreated() == null) {
        ((HasCreated) entity).setCreated(ZonedDateTime.now());
      }
    }
  }
}
