package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.service.api.invoke.CommonExecContext;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 06. 22.
 */
@SuppressWarnings("ClassCanBeRecord")
public class JoinEntityHelper<E extends Entity, JD extends Dto, JE extends Entity, JID> {

  public interface JoinApplier<E extends Entity, JE extends Entity> {
    void apply(E entity, List<JE> joined);
  }

  private final EntityByIdService<JD, JE, JID> service;
  private final List<E> parentEntities;

  public JoinEntityHelper(EntityByIdService<JD, JE, JID> service, List<E> parentEntities) {
    this.service = service;
    this.parentEntities = parentEntities;
  }

  public List<JE> load(Supplier<List<JID>> idSupplier) {
    List<JID> ids = idSupplier.get();
    if (!ids.iterator().hasNext()) {
      throw new ServiceException(ErrorCode.parameter_validation_error, "Missing parameter ids for bound model!");
    }
    List<JE> entities = service.findById(ids);
    if (entities.size() != ids.size()) {
      throw new ServiceException(ErrorCode.data_validation_error, "Loaded entities size does not match parameters size!");
    }
    return entities;
  }

  public <D extends Dto, P extends Params> List<E> apply(Supplier<List<JID>> joinedIdSupplier,
                                                         CommonExecContext<P, D> ctx,
                                                         JoinApplier<E, JE> joinApplier) {
    List<JE> joined = load(joinedIdSupplier);
    for (E entity : parentEntities) {
      if (!ctx.getSubject().isPermitted(ctx.getSecurityDomainAction(entity.identifier()))) {
        throw new ServiceException(ErrorCode.authorization_error, "Update for entity not permitted!",
          Set.of(new AttributeString(entity.identifierField(), entity.identifier())));
      }
      joinApplier.apply(entity, joined);
    }
    return parentEntities;
  }
}
