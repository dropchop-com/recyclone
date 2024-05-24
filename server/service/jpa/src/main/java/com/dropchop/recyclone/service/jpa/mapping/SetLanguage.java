package com.dropchop.recyclone.service.jpa.mapping;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.HasLanguageCode;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.mapper.api.AfterToEntityListener;
import com.dropchop.recyclone.service.api.mapping.EntityAllPreloadDelegate;
import com.dropchop.recyclone.service.jpa.localization.LanguageService;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
@SuppressWarnings("unused")
public class SetLanguage
  extends EntityAllPreloadDelegate<Language, JpaLanguage, String>
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
      if (entity instanceof HasLanguageCode && entity instanceof HasJpaLanguage) {
        String code = ((HasLanguageCode) entity).getLang();
        if (code == null) {
          throw new ServiceException(ErrorCode.internal_error, "Missing language code in lang field for DTO!",
            Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
        }
        JpaLanguage lang = this.findById(new Language(code));
        if (lang != null) {
          ((HasJpaLanguage) entity).setLanguage(lang);
        }
      }
    }
  }
}
