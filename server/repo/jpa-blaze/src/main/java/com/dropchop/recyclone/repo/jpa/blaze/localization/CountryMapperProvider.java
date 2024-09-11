package com.dropchop.recyclone.repo.jpa.blaze.localization;

import com.dropchop.recyclone.mapper.jpa.localization.CountryToDtoMapper;
import com.dropchop.recyclone.mapper.jpa.localization.CountryToJpaMapper;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
import com.dropchop.recyclone.repo.jpa.blaze.RecycloneMapperProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 09. 24.
 */
@Getter
@ApplicationScoped
public class CountryMapperProvider extends RecycloneMapperProvider<Country, JpaCountry, String> {

  @Inject
  CountryRepository repository;

  @Inject
  CountryToDtoMapper toDtoMapper;

  @Inject
  CountryToJpaMapper toEntityMapper;
}
