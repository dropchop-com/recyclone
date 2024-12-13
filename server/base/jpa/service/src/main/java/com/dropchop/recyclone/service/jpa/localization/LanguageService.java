package com.dropchop.recyclone.service.jpa.localization;

import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class LanguageService extends CrudServiceImpl<Language, JpaLanguage, String>
  implements com.dropchop.recyclone.service.api.localization.LanguageService {

  @Inject
  LanguageRepository repository;

  @Inject
  LanguageMapperProvider mapperProvider;
}
