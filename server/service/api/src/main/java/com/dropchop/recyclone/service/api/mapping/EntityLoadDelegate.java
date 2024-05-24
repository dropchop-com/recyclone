package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.mapper.api.MappingContext;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 31. 05. 22.
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public class EntityLoadDelegate<D extends Dto, E extends Entity, ID> {
  private final EntityByIdService<D, E, ID> service;
  private final Set<String> onlyForRegisteredActions = new HashSet<>();

  private boolean failIfMissing = true;
  private boolean failIfPresent = false;

  public EntityLoadDelegate(EntityByIdService<D, E, ID> service) {
    this.service = service;
  }

  public Class<E> getEntityType() {
    return service.getRootClass();
  }

  public EntityByIdService<D, E, ID> getService() {
    return service;
  }

  public EntityLoadDelegate<D, E, ID> forActionOnly(String action) {
    onlyForRegisteredActions.add(action);
    return this;
  }

  public EntityLoadDelegate<D, E, ID> failIfMissing(boolean failIfMissing) {
    this.failIfMissing = failIfMissing;
    return this;
  }

  public EntityLoadDelegate<D, E, ID> failIfPresent(boolean failIfPresent) {
    this.failIfPresent = failIfPresent;
    return this;
  }

  protected E findById(D dto) {
    return service.findById(dto);
  }

  public E load(D dto, MappingContext context) {
    if (
      onlyForRegisteredActions.isEmpty() ||
        onlyForRegisteredActions.contains(context.getSecurityAction())
    ) {
      String identifier = dto.identifier();
      if ((identifier == null || identifier.isBlank())) {
        if (failIfMissing) {
          throw new ServiceException(ErrorCode.data_validation_error, "Missing identifier for DTO!",
            Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
        }
        return null;
      }
      E entity = findById(dto);
      if (entity == null) {
        if (failIfMissing) {
          throw new ServiceException(ErrorCode.data_validation_error, "Missing entity for DTO identifier!",
            Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
        }
        return null;
      }
      if (failIfPresent) {
        throw new ServiceException(ErrorCode.data_validation_error, "Entity for DTO identifier is already present!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
      return entity;
    }

    return null;
  }

}
