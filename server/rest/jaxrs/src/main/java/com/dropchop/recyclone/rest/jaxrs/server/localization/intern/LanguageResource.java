package com.dropchop.recyclone.rest.jaxrs.server.localization.intern;

import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.rest.jaxrs.api.ClassicRestResource;
import com.dropchop.recyclone.service.api.localization.LanguageService;
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
public class LanguageResource implements
    ClassicRestResource<Language>,
    com.dropchop.recyclone.rest.jaxrs.api.intern.localization.LanguageResource {

  @Inject
  LanguageService service;

  @Override
  public Result<Language> create(List<Language> languages) {
    return service.create(languages);
  }

  @Override
  public List<Language> createRest(List<Language> languages) {
    return unwrap(create(languages));
  }

  @Override
  public Result<Language> delete(List<Language> languages) {
    return service.delete(languages);
  }

  @Override
  public List<Language> deleteRest(List<Language> languages) {
    return unwrap(delete(languages));
  }

  @Override
  public Result<Language> update(List<Language> languages) {
    return service.update(languages);
  }

  @Override
  public List<Language> updateRest(List<Language> languages) {
    return unwrap(update(languages));
  }
}
