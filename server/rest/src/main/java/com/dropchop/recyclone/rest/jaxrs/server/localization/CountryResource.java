package com.dropchop.recyclone.rest.jaxrs.server.localization;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.ClassicReadByCodeResource;
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
public class CountryResource extends ClassicReadByCodeResource<Country, CodeParams> implements
  com.dropchop.recyclone.rest.jaxrs.api.localization.CountryResource {

  @Inject
  CountryService service;

  @Inject
  CodeParams params;

  @Override
  public Result<Country> get() {
    return service.search();
  }

  @Override
  public Result<Country> getByCode(String code) {
    params.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<Country> search(CodeParams parameters) {
    return service.search();
  }
}
