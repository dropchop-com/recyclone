package com.dropchop.recyclone.rest.jaxrs.server.localization.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.localization.CountryService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
@Path(Paths.INTERNAL_SEGMENT + Paths.Localization.COUNTRY)
public class CountryResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.localization.CountryResource {

  @Inject
  ServiceSelector selector;

  @Override
  public Result<Country> create(List<Country> countries) {
    return selector.select(CountryService.class).create(countries);
  }

  @Override
  public Result<Country> delete(List<Country> countries) {
    return selector.select(CountryService.class).delete(countries);
  }

  @Override
  public Result<Country> update(List<Country> countries) {
    return selector.select(CountryService.class).update(countries);
  }
}
