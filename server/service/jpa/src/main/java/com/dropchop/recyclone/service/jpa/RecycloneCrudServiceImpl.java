package com.dropchop.recyclone.service.jpa;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.quarkus.runtime.service.ServiceSelector;
import com.dropchop.recyclone.service.jpa.localization.LanguageService;
import com.dropchop.recyclone.service.jpa.mapping.SetLanguage;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 03. 22.
 */
public abstract class RecycloneCrudServiceImpl<D extends Dto, E extends Entity, ID>
  extends CrudServiceImpl<D, E, ID> {

  //TODO: refactor this
  @Inject
  ServiceSelector serviceSelector;

  protected MappingContext getMappingContextForModify() {
    Class<?> rootClass = getRepository().getRootClass();
    MappingContext context = super.getMappingContextForModify();
    context.afterMapping(
      new SetLanguage(serviceSelector.select(LanguageService.class, this.getClass()), rootClass)
    );
    return context;
  }
}
