package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 06. 22.
 */
public class ServiceJoinEntityHelper<PE extends Entity, D extends Dto, E extends Entity, ID> {

  public interface JoinApplier<PE extends Entity, E extends Entity> {
    void apply(PE entity, List<E> joined);
  }

  private final EntityByIdService<D, E, ID> service;
  private final List<PE> parentEntities;

  public ServiceJoinEntityHelper(EntityByIdService<D, E, ID> service, List<PE> parentEntities) {
    this.service = service;
    this.parentEntities = parentEntities;
  }

  public List<E> load(Supplier<List<ID>> idSupplier) {
    List<ID> ids = idSupplier.get();
    if (!ids.iterator().hasNext()) {
      throw new ServiceException(ErrorCode.parameter_validation_error, "Missing parameter ids for bound model!");
    }
    List<E> entities = service.findById(ids);
    if (entities.size() != entities.size()) {
      throw new ServiceException(ErrorCode.data_validation_error, "Loaded entities size does not match parameters size!");
    }
    return entities;
  }

  public <PD extends Dto, PP extends Params> List<PE> apply(Supplier<List<ID>> joinedIdSupplier,
                                                            CommonExecContext<PP, PD> ctx,
                                                            JoinApplier<PE, E> joinApplier) {
    List<E> joined = load(joinedIdSupplier);
    for (PE entity : parentEntities) {
      if (!ctx.getSubject().isPermitted(ctx.getSecurityDomainAction(entity.identifier()))) {
        throw new ServiceException(ErrorCode.authorization_error, "Update for entity not permitted!",
          Set.of(new AttributeString(entity.identifierField(), entity.identifier())));
      }
      joinApplier.apply(entity, joined);
    }
    return parentEntities;
  }
}
