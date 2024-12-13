package com.dropchop.recyclone.repo.jpa.blaze;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.repo.FilteringMapperProvider;
import com.dropchop.recyclone.repo.jpa.blaze.localization.LanguageRepository;
import com.dropchop.recyclone.repo.jpa.blaze.mapping.SetLanguage;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 09. 24.
 */
public abstract class RecycloneMapperProvider<D extends Dto, E extends Entity, ID>
    extends FilteringMapperProvider<D, E, ID> {

  @Inject
  LanguageRepository languageRepository;

  public MappingContext getMappingContextForModify() {
    Class<?> rootClass = getRepository().getRootClass();
    MappingContext context = super.getMappingContextForModify();
    context.afterMapping(
        new SetLanguage(languageRepository, rootClass)
    );
    return context;
  }
}
