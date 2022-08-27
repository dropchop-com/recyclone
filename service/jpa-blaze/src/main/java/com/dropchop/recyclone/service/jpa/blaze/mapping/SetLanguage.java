package com.dropchop.recyclone.service.jpa.blaze.mapping;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.HasLanguageCode;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.api.mapping.AfterToEntityListener;
import com.dropchop.recyclone.service.api.mapping.EntityAllPreloadDelegate;
import com.dropchop.recyclone.service.jpa.blaze.localization.LanguageService;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
public class SetLanguage
  extends EntityAllPreloadDelegate<Language, ELanguage, String>
  implements AfterToEntityListener {

  final Class<?> onlyForEntity;

  public SetLanguage(LanguageService service) {
    super(service);
    this.onlyForEntity = null;
  }

  public SetLanguage(LanguageService service, Class<?> onlyForEntity) {
    super(service);
    this.onlyForEntity = onlyForEntity;
  }

  @Override
  public void after(Model dto, Entity entity, MappingContext context) {
    if (onlyForEntity != null && !onlyForEntity.isAssignableFrom(entity.getClass())) {
      return;
    }
    if (Constants.Actions.CREATE.equals(context.getSecurityAction())) { // for other actions it has no sense
      if (entity instanceof HasLanguageCode && entity instanceof HasELanguage) {
        String code = ((HasLanguageCode) entity).getLang();
        if (code == null) {
          throw new ServiceException(ErrorCode.internal_error, "Missing language code in lang field for DTO!",
            Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
        }
        ELanguage lang = this.findById(new Language(code));
        if (lang != null) {
          ((HasELanguage) entity).setLanguage(lang);
        }
      }
    }
  }
}
