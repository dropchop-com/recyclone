package com.dropchop.recyclone.service.jpa.localization;

import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.service.RecycloneType;
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
  implements com.dropchop.recyclone.base.api.service.localization.LanguageService {

  @Inject
  LanguageRepository repository;

  @Inject
  LanguageMapperProvider mapperProvider;
}