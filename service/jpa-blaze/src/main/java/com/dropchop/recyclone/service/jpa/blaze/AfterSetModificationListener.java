package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.localization.Translation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.service.api.mapping.AfterToEntityListener;
import com.dropchop.recyclone.service.api.invoke.MappingContext;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public class AfterSetModificationListener<P extends Params>
  implements AfterToEntityListener<P> {

  @Override
  public void after(Dto dto, Entity entity, MappingContext<P> context) {
    if (Actions.CREATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasCreated) {
        ((HasCreated) entity).setCreated(ZonedDateTime.now());
      }
      if (entity instanceof HasTitleTranslation<?>) {
        Set<? extends Translation> translations = ((HasTitleTranslation<?>) entity).getTranslations();
        if (translations != null) {
          for (Translation t : translations) {
            if (t instanceof HasCreated) {
              ((HasCreated) t).setCreated(ZonedDateTime.now());
            }
            if (t instanceof HasModified) {
              ((HasModified) t).setModified(ZonedDateTime.now());
            }
          }
        }
      }
    } else if (Actions.UPDATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasTitleTranslation<?>) {
        Set<? extends Translation> translations = ((HasTitleTranslation<?>) entity).getTranslations();
        if (translations != null) {
          for (Translation t : translations) {
            if (t instanceof HasModified) {
              ((HasModified) t).setModified(ZonedDateTime.now());
            }
          }
        }
      }
    }
    if (
      entity instanceof HasModified &&
        (Actions.CREATE.equals(context.getSecurityAction()) || Actions.UPDATE.equals(context.getSecurityAction()))
    ) {
      ((HasModified) entity).setModified(ZonedDateTime.now());
    }
  }
}
