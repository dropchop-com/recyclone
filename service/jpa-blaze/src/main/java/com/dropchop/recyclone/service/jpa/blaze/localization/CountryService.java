package com.dropchop.recyclone.service.jpa.blaze.localization;

import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepository;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.blaze.RecycloneCrudServiceImpl;
import com.dropchop.recyclone.service.api.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class CountryService extends RecycloneCrudServiceImpl<Country, ECountry, String>
  implements com.dropchop.recyclone.service.api.localization.CountryService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  CountryRepository repository;

  @Inject
  CountryToDtoMapper toDtoMapper;

  @Inject
  CountryToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Country, ECountry, String> getConfiguration() {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper
    );
  }
}
