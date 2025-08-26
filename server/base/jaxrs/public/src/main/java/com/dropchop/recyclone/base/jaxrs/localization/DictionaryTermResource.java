package com.dropchop.recyclone.base.jaxrs.localization;

import com.dropchop.recyclone.base.api.rest.ClassicReadByCodeResource;
import com.dropchop.recyclone.base.api.service.localization.DictionaryTermService;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.dto.model.rest.Result;
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
public class DictionaryTermResource extends ClassicReadByCodeResource<DictionaryTerm, CodeParams> implements
    com.dropchop.recyclone.base.api.jaxrs.localization.DictionaryTermResource {

  @Inject
  DictionaryTermService service;

  @Inject
  CodeParams params;

  @Override
  public Result<DictionaryTerm> get() {
    return service.search();
  }

  @Override
  public Result<DictionaryTerm> getByCode(String code) {
    params.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<DictionaryTerm> search(CodeParams parameters) {
    return service.search();
  }
}
