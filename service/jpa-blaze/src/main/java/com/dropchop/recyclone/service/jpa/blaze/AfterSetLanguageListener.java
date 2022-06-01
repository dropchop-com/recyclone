package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.marker.HasLanguageCode;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import com.dropchop.recyclone.service.api.mapping.AfterToEntityListener;
import com.dropchop.recyclone.service.api.mapping.EntityAllPreloadDelegate;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import com.dropchop.recyclone.service.jpa.blaze.localization.LanguageService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
public class AfterSetLanguageListener<D extends Dto, P extends Params>
  extends EntityAllPreloadDelegate<Language, ELanguage, String, P>
  implements AfterToEntityListener<P> {

  public <E extends Entity> AfterSetLanguageListener(LanguageService service) {
    super(service);
  }

  @Override
  public void after(Dto dto, Entity entity, MappingContext<P> context) {
    if (Constants.Actions.CREATE.equals(context.getSecurityAction())) {
      if (entity instanceof HasLanguageCode && entity instanceof HasELanguage) {
        // load language only for root entities
        List<Dto> data = context.getData();
        if (data != null && data.contains(dto)) {
          String code = ((HasLanguageCode) entity).getLang();
          if (code == null) {
            throw new ServiceException(ErrorCode.internal_error, "Missing language code in lang field for DTO!",
              Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
          }
          Optional<ELanguage> lang = this.findById(new Language(code));
          lang.ifPresent(eLanguage -> ((HasELanguage) entity).setLanguage(eLanguage));
        }
      }
    }
  }
}
