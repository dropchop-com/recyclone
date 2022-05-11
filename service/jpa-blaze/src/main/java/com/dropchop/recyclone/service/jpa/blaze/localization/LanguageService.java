package com.dropchop.recyclone.service.jpa.blaze.localization;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.Implementation;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@Implementation(JPA_DEFAULT)
public class LanguageService extends CrudServiceImpl<Language, CodeParams, ELanguage, String>
  implements com.dropchop.recyclone.service.api.localization.LanguageService, EntityByIdService<Language, ELanguage, String> {

  @Inject
  LanguageRepository repository;

  @Inject
  LanguageToDtoMapper toDtoMapper;

  @Inject
  LanguageToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Language, CodeParams, ELanguage, String> getConfiguration(CommonExecContext<CodeParams, Language> ctx) {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper,
      ctx
    );
  }
}
