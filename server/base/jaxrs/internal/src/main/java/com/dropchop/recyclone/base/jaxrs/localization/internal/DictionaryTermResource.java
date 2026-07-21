package com.dropchop.recyclone.base.jaxrs.localization.internal;

import com.dropchop.recyclone.base.api.rest.ClassicModifyResource;
import com.dropchop.recyclone.base.api.service.localization.DictionaryTermService;
import com.dropchop.recyclone.base.dto.model.localization.DictionaryTerm;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 06. 22.
 */
@Slf4j
@RequestScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class DictionaryTermResource extends ClassicModifyResource<DictionaryTerm> implements
    com.dropchop.recyclone.base.api.jaxrs.internal.localization.DictionaryTermResource {

  @Inject
  DictionaryTermService service;

  @Override
  public Result<DictionaryTerm> create(List<DictionaryTerm> terms) {
    return service.create(terms);
  }

  @Override
  public Result<DictionaryTerm> delete(List<DictionaryTerm> terms) {
    return service.delete(terms);
  }

  @Override
  public Result<DictionaryTerm> update(List<DictionaryTerm> terms) {
    return service.update(terms);
  }
}
