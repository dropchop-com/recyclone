package com.dropchop.recyclone.base.api.repo.mapper;

import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.dto.model.base.DtoCode;
import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.dropchop.recyclone.base.api.repo.ReadRepository;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 05. 22.
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public abstract class EntityLoadDelegate<D extends Dto, E extends Entity, ID> extends EntityDelegate<D, E> {
  private final ReadRepository<E, ID> repository;

  public EntityLoadDelegate(ReadRepository<E, ID> repository) {
    super(Set.of(repository.getRootClass()));
    this.repository = repository;
  }

  public ReadRepository<E, ID> getRepository() {
    return repository;
  }

  @Override
  public EntityLoadDelegate<D, E, ID> forActionOnly(String action) {
    super.forActionOnly(action);
    return this;
  }

  @Override
  public EntityLoadDelegate<D, E, ID> failIfMissing(boolean failIfMissing) {
    super.failIfMissing(failIfMissing);
    return this;
  }

  @Override
  public EntityLoadDelegate<D, E, ID> failIfPresent(boolean failIfPresent) {
    super.failIfPresent(failIfPresent);
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
    if (this.isForActions(context.getSecurityAction())) {
      String identifier = dto.identifier();
      if ((identifier == null || identifier.isBlank())) {
        if (this.isFailIfMissing()) {
          throw new ServiceException(
              ErrorCode.data_validation_error, "Missing identifier for DTO!",
              Set.of(new AttributeString(dto.identifierField(), dto.identifier()))
          );
        }
        return null;
      }
      E entity = findById(dto);
      if (entity == null) {
        if (this.isFailIfMissing()) {
          throw new ServiceException(
              ErrorCode.data_validation_error, "Missing entity for DTO identifier!",
              Set.of(new AttributeString(dto.identifierField(), dto.identifier()))
          );
        }
        return null;
      }
      if (this.isFailIfPresent()) {
        throw new ServiceException(
            ErrorCode.data_validation_error, "Entity for DTO identifier is already present!",
            Set.of(new AttributeString(dto.identifierField(), dto.identifier()))
        );
      }
      return entity;
    }

    return null;
  }
}
