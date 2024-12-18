package com.dropchop.recyclone.base.api.rest.localization;

import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.localization.Country;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.api.rest.ClassicReadByCodeResource;
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
public class CountryResource extends ClassicReadByCodeResource<Country, CodeParams> implements
    com.dropchop.recyclone.base.api.jaxrs.localization.CountryResource {

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