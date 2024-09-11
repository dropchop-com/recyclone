package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.mapper.jpa.localization.LanguageToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.localization.LanguageToJpaMapper;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class LanguageMapperProvider extends RecycloneMapperProvider<Language, JpaLanguage, String> {

  @Inject
  LanguageRepository repository;

  @Inject
  LanguageToDtoMapper toDtoMapper;

  @Inject
  LanguageToJpaMapper toEntityMapper;
}
