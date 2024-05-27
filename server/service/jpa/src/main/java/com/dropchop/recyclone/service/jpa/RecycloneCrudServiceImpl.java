package com.dropchop.recyclone.service.jpa;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.repo.api.ctx.CriteriaDecorator;
import com.dropchop.recyclone.repo.api.ctx.RepositoryExecContext;
import com.dropchop.recyclone.repo.jpa.blaze.*;
import com.dropchop.recyclone.service.api.CrudServiceImpl;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.jpa.localization.LanguageService;
import com.dropchop.recyclone.service.jpa.mapping.SetLanguage;
import jakarta.inject.Inject;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 03. 22.
 */
public abstract class RecycloneCrudServiceImpl<D extends Dto, E extends Entity, ID>
  extends CrudServiceImpl<D, E, ID> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  ExecContextContainer ctxContainer;

  //TODO: refactor this
  @Inject
  ServiceSelector serviceSelector;

  protected Collection<CriteriaDecorator> getCommonCriteriaDecorators() {
    return List.of(
      new LikeIdentifiersCriteriaDecorator(),
      new InlinedStatesCriteriaDecorator(),
      new SortCriteriaDecorator(),
      new PageCriteriaDecorator()
    );
  }

  protected RepositoryExecContext<E> getRepositoryExecContext() {
    BlazeExecContext<E> context = new BlazeExecContext<E>().of(ctxContainer.get());
    for (CriteriaDecorator decorator : getCommonCriteriaDecorators()) {
      context.decorateWith(decorator);
    }
    return context;
  }

  protected MappingContext getMappingContextForModify() {
    Class<?> rootClass = getRepository().getRootClass();
    MappingContext context = super.getMappingContextForModify();
    context.afterMapping(
      new SetLanguage(serviceSelector.select(LanguageService.class), rootClass)
    );
    return context;
  }
}
