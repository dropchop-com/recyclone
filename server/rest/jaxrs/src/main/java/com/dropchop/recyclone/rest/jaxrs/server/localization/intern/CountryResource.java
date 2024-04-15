package com.dropchop.recyclone.rest.jaxrs.server.localization.intern;

import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.service.api.localization.CountryService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class CountryResource implements
    ClassicRestResource<Country>,
    com.dropchop.recyclone.rest.jaxrs.api.intern.localization.CountryResource {

  @Inject
  CountryService service;

  @Override
  public Result<Country> create(List<Country> countries) {
    return service.create(countries);
  }

  @Override
  public List<Country> createRest(List<Country> countries) {
    return unwrap(create(countries));
  }

  @Override
  public Result<Country> delete(List<Country> countries) {
    return service.delete(countries);
  }

  @Override
  public List<Country> deleteRest(List<Country> countries) {
    return unwrap(delete(countries));
  }

  @Override
  public Result<Country> update(List<Country> countries) {
    return service.update(countries);
  }

  @Override
  public List<Country> updateRest(List<Country> countries) {
    return unwrap(update(countries));
  }
}
