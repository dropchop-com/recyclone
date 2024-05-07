package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class LanguageRepository extends BlazeRepository<JpaLanguage, String> {

  @Override
  public Class<JpaLanguage> getRootClass() {
    return JpaLanguage.class;
  }
}
