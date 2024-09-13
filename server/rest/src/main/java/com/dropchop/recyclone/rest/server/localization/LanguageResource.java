package com.dropchop.recyclone.rest.server.localization;

import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.server.ClassicReadByCodeResource;
import com.dropchop.recyclone.service.api.localization.LanguageService;
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
public class LanguageResource extends ClassicReadByCodeResource<Language, CodeParams> implements
    com.dropchop.recyclone.rest.api.localization.LanguageResource {

  @Inject
  LanguageService service;

  @Inject
  CodeParams params;

  @Override
  public Result<Language> get() {
    return service.search();
  }

  @Override
  public Result<Language> getByCode(String code) {
    params.setCodes(List.of(code));
    return service.search();
  }

  @Override
  public Result<Language> search(CodeParams parameters) {
    return service.search();
  }
}
