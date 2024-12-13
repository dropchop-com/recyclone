package com.dropchop.recyclone.repo.api.mapper;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.repo.api.ReadRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public class EntityLoadDelegate<D extends Dto, E extends Entity, ID> {
  private final ReadRepository<E, ID> repository;
  private final Set<String> onlyForRegisteredActions = new HashSet<>();

  private boolean failIfMissing = true;
  private boolean failIfPresent = false;

  public EntityLoadDelegate(ReadRepository<E, ID> repository) {
    this.repository = repository;
  }

  public Class<E> getEntityType() {
    return repository.getRootClass();
  }

  public ReadRepository<E, ID> getRepository() {
    return repository;
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
    if (dto instanceof DtoCode) {
      //noinspection unchecked
      return repository.findById((ID)((DtoCode) dto).getCode());
    } else if (dto instanceof DtoId) {
      //noinspection unchecked
      return repository.findById((ID)((DtoId) dto).getUuid());
    } else {
      throw new RuntimeException("findById(" + dto + ") is not supported.");
    }
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
