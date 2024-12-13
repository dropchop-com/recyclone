package com.dropchop.recyclone.rest.server.localization.intern;

import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.rest.server.ClassicModifyResource;
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
public class LanguageResource extends ClassicModifyResource<Language> implements
    com.dropchop.recyclone.rest.api.internal.localization.LanguageResource {

  @Inject
  LanguageService service;

  @Override
  public Result<Language> create(List<Language> languages) {
    return service.create(languages);
  }

  @Override
  public Result<Language> delete(List<Language> languages) {
    return service.delete(languages);
  }

  @Override
  public Result<Language> update(List<Language> languages) {
    return service.update(languages);
  }
}
