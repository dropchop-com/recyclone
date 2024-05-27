package com.dropchop.recyclone.service.jpa.localization;

import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.RecycloneCrudServiceImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@ServiceType(RECYCLONE_JPA_DEFAULT)
public class LanguageService extends RecycloneCrudServiceImpl<Language, JpaLanguage, String>
  implements com.dropchop.recyclone.service.api.localization.LanguageService {

  @Inject
  @RepositoryType(RECYCLONE_JPA_DEFAULT)
  LanguageRepository repository;
}
