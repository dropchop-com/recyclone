package com.dropchop.recyclone.service.jpa.blaze.localization;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.entity.jpa.localization.ECountry;
import com.dropchop.recyclone.repo.api.RepositoryType;
import com.dropchop.recyclone.repo.jpa.blaze.localization.CountryRepository;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.ServiceType;
import com.dropchop.recyclone.service.jpa.blaze.CrudServiceImpl;
import com.dropchop.recyclone.service.jpa.blaze.ServiceConfiguration;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RCYN_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
@Slf4j
@ApplicationScoped
@ServiceType(RCYN_DEFAULT)
public class CountryService extends CrudServiceImpl<Country, CodeParams, ECountry, String>
  implements com.dropchop.recyclone.service.api.localization.CountryService {

  @Inject
  @RepositoryType(RCYN_DEFAULT)
  CountryRepository repository;

  @Inject
  CountryToDtoMapper toDtoMapper;

  @Inject
  CountryToEntityMapper toEntityMapper;

  @Override
  public ServiceConfiguration<Country, CodeParams, ECountry, String> getConfiguration(CommonExecContext<CodeParams, Country> ctx) {
    return new ServiceConfiguration<>(
      repository,
      toDtoMapper,
      toEntityMapper,
      ctx
    );
  }
}
