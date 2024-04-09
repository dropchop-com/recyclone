package com.dropchop.recyclone.rest.jaxrs.server.localization.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.localization.LanguageService;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
//@Path(Paths.INTERNAL_SEGMENT + Paths.Localization.LANGUAGE)
public class LanguageResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.localization.LanguageResource {

  @Inject
  ServiceSelector selector;

  @Override
  public Result<Language> create(List<Language> languages) {
    return selector.select(LanguageService.class).create(languages);
  }

  @Override
  public Result<Language> delete(List<Language> languages) {
    return selector.select(LanguageService.class).delete(languages);
  }

  @Override
  public Result<Language> update(List<Language> languages) {
    return selector.select(LanguageService.class).update(languages);
  }
}
