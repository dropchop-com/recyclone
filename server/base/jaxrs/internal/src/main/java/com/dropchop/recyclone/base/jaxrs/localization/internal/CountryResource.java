package com.dropchop.recyclone.base.jaxrs.localization.internal;

import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.api.rest.ClassicModifyResource;
import com.dropchop.recyclone.base.api.service.localization.CountryService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class CountryResource extends ClassicModifyResource<Country> implements
    com.dropchop.recyclone.base.api.jaxrs.internal.localization.CountryResource {

  @Inject
  CountryService service;

  @Override
  public Result<Country> create(List<Country> countries) {
    return service.create(countries);
  }

  @Override
  public Result<Country> delete(List<Country> countries) {
    return service.delete(countries);
  }

  @Override
  public Result<Country> update(List<Country> countries) {
    return service.update(countries);
  }
}
