package com.dropchop.recyclone.base.jpa.repo.localization;

import com.dropchop.recyclone.base.jpa.mapper.localization.CountryToDtoMapper;
import com.dropchop.recyclone.base.jpa.mapper.localization.CountryToJpaMapper;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaCountry;
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
public class CountryMapperProvider extends RecycloneMapperProvider<Country, JpaCountry, String> {

  @Inject
  CountryRepository repository;

  @Inject
  CountryToDtoMapper toDtoMapper;

  @Inject
  CountryToJpaMapper toEntityMapper;
}
