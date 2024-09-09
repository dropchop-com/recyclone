package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.mapper.jpa.localization.CountryToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.localization.CountryToJpaMapper;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.repo.api.CrudServiceRepository;
import com.dropchop.recyclone.repo.jpa.blaze.BlazeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 02. 22.
 */
@Getter
@ApplicationScoped
public class CountryRepository extends BlazeRepository<JpaCountry, String>
    implements CrudServiceRepository<Country, JpaCountry, String> {

  Class<JpaCountry> rootClass = JpaCountry.class;

  @Inject
  CountryToDtoMapper toDtoMapper;

  @Inject
  CountryToJpaMapper toEntityMapper;
}
