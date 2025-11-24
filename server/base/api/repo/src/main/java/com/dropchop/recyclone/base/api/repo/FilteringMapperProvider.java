package com.dropchop.recyclone.base.api.repo;

import com.dropchop.recyclone.base.api.mapper.FilteringDtoContext;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.mapper.SetEntityDeactivated;
import com.dropchop.recyclone.base.api.mapper.SetEntityModification;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.repo.mapper.EntityLoadDelegateFactory;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 09. 24.
 */
public abstract class FilteringMapperProvider<D extends Dto, E extends Entity, ID> implements MapperProvider<D, E> {

  public MappingContext getMappingContextForRead(CommonExecContext<?, ?> sourceContext) {
    return new FilteringDtoContext().of(sourceContext);
  }

  public MappingContext getMappingContextForModify(CommonExecContext<?, ?> sourceContext) {
    Class<?> rootClass = getRepository().getRootClass();
    return new FilteringDtoContext()
        .of(sourceContext)
        .createWith(
            new EntityLoadDelegateFactory<>(getRepository())
                .forActionOnly(Constants.Actions.UPDATE)
                .forActionOnly(Constants.Actions.DELETE)
        )
        .afterMapping(
            new SetEntityModification(rootClass)
        )
        .afterMapping(
            new SetEntityDeactivated(rootClass)
        );
  }

  public abstract CrudRepository<E, ID> getRepository();
}
