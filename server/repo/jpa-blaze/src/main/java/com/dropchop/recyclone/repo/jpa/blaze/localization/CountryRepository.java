package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;

import jakarta.enterprise.context.ApplicationScoped;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@ApplicationScoped
@RepositoryType(RECYCLONE_JPA_DEFAULT)
public class CountryRepository extends BlazeRepository<JpaCountry, String> {

  @Override
  public Class<JpaCountry> getRootClass() {
    return JpaCountry.class;
  }
}
