package com.dropchop.recyclone.base.jpa.repo.localization;

import com.dropchop.recyclone.base.jpa.mapper.localization.LanguageToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.LanguageToJpaMapper;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.base.jpa.repo.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
@SuppressWarnings("unused")
public class LanguageMapperProvider extends RecycloneMapperProvider<Language, JpaLanguage, String> {

  @Inject
  LanguageRepository repository;

  @Inject
  LanguageToDtoMapper toDtoMapper;

  @Inject
  LanguageToJpaMapper toEntityMapper;
}
