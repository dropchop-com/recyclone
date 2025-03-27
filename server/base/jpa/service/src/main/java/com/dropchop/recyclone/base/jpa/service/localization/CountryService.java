package com.dropchop.recyclone.base.jpa.service.localization;

import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.service.CrudServiceImpl;
import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.jpa.model.localization.JpaCountry;
import com.dropchop.recyclone.base.jpa.repo.localization.CountryMapperProvider;
import com.dropchop.recyclone.base.jpa.repo.localization.CountryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
@Getter
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
@SuppressWarnings("unused")
public class CountryService extends CrudServiceImpl<Country, JpaCountry, String>
  implements com.dropchop.recyclone.base.api.service.localization.CountryService {

  @Inject
  CountryRepository repository;

  @Inject
  CountryMapperProvider mapperProvider;

  @Inject
  CommonExecContext<Country, ?> executionContext;
}
