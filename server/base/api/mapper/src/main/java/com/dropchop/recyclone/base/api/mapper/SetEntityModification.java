package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.Translation;
import com.dropchop.recyclone.base.api.model.marker.HasTranslation;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.security.Constants.Actions;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
@SuppressWarnings("unused")
public class SetEntityModification
  extends RestrictedAfterToEntityListener {

  public SetEntityModification() {
  }

  public SetEntityModification(Class<?> onlyForEntity) {
    super(onlyForEntity);
  }

  @Override
  public void after(Model model, Entity entity, MappingContext context) {
    if (onlyForEntity != null && !onlyForEntity.isAssignableFrom(entity.getClass())) {
      return;
    }
    boolean isCreateOrUpdate = Actions.CREATE.equals(context.getSecurityAction()) || Actions.UPDATE.equals(context.getSecurityAction());
    if (isCreateOrUpdate) {
      if (entity instanceof HasModified && ((HasModified) entity).getModified() == null) {
        ((HasModified) entity).setModified(ZonedDateTime.now());
      }
      if (entity instanceof HasTranslation<?> && model instanceof HasTranslation<?>) {
        Set<? extends Translation> dtoTranslations = ((HasTranslation<?>) model).getTranslations();
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
