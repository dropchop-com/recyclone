package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.mapper.jpa.localization.LanguageToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.localization.LanguageToJpaMapper;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class LanguageRepository extends BlazeRepository<JpaLanguage, String>
    implements CrudServiceRepository<Language, JpaLanguage, String> {

  Class<JpaLanguage> rootClass = JpaLanguage.class;

  @Inject
  LanguageToDtoMapper toDtoMapper;

  @Inject
  LanguageToJpaMapper toEntityMapper;
}
