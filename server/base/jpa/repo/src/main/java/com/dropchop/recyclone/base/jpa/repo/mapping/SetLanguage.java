package com.dropchop.recyclone.base.jpa.repo.mapping;

import com.dropchop.recyclone.base.api.mapper.AfterToEntityListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.marker.HasLanguageCode;
import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
import com.dropchop.recyclone.base.api.repo.mapper.EntityAllPreloadDelegate;
import com.dropchop.recyclone.base.jpa.repo.localization.LanguageRepository;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
@SuppressWarnings("unused")
public class SetLanguage
  extends EntityAllPreloadDelegate<Language, JpaLanguage, String>
  implements AfterToEntityListener {

  final Class<?> onlyForEntity;

  public SetLanguage(LanguageRepository repository) {
    super(repository);
    this.onlyForEntity = null;
  }

  public SetLanguage(LanguageRepository repository, Class<?> onlyForEntity) {
    super(repository);
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
