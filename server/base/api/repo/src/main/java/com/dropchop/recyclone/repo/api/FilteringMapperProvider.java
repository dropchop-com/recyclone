package com.dropchop.recyclone.repo.api;

import com.dropchop.recyclone.mapper.api.*;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.repo.api.mapper.EntityDelegateFactory;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
public abstract class FilteringMapperProvider<D extends Dto, E extends Entity, ID> implements MapperProvider<D, E> {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContextContainer ctxContainer;

  public MappingContext getMappingContextForRead() {
    MappingContext context = new FilteringDtoContext().of(ctxContainer.get());
    //log.debug("Created mapping context [{}] for reading from execution context [{}].", context, ctxContainer.get());
    return context;
  }

  public MappingContext getMappingContextForModify() {
    Class<?> rootClass = getRepository().getRootClass();
    MappingContext context = new FilteringDtoContext()
        .of(ctxContainer.get())
        .createWith(
            new EntityDelegateFactory<>(getRepository())
                .forActionOnly(Constants.Actions.UPDATE)
                .forActionOnly(Constants.Actions.DELETE)
        )
        .afterMapping(
            new SetEntityModification(rootClass)
        )
        .afterMapping(
            new SetEntityDeactivated(rootClass)
        );

    //log.debug("Created mapping context [{}] for modification from execution context [{}].", context, ctxContainer.get());
    return context;
  }

  public abstract CrudRepository<E, ID> getRepository();
}
