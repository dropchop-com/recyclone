package com.dropchop.recyclone.service.jpa.blaze;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.mapping.CreateEntityListener;
import com.dropchop.recyclone.service.api.mapping.MappingContext;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 05. 22.
 */
public class RelatedEntityLoader<D extends Dto, E extends Entity, ID, P extends Params>
  implements CreateEntityListener<D, E, P> {

  private final EntityByIdService<D, E, ID> service;
  private final Set<String> onlyForRegisteredActions = new HashSet<>();

  public RelatedEntityLoader(EntityByIdService<D, E, ID> service) {
    this.service = service;
  }

  @Override
  public Class<E> getEntityType() {
    return service.getRootClass();
  }

  public RelatedEntityLoader<D, E, ID, P> forActionOnly(String action) {
    onlyForRegisteredActions.add(action);
    return this;
  }

  protected Optional<E> findById(D dto) {
    return service.findById(dto);
  }

  @Override
  public E create(D dto, MappingContext<P> context) {
    if (
      onlyForRegisteredActions.isEmpty() ||
      onlyForRegisteredActions.contains(context.getSecurityAction())
    ) {
      String identifier = dto.identifier();
      if (identifier == null || identifier.isBlank()) {
        throw new ServiceException(ErrorCode.data_validation_error, "Missing identifier for DTO!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
      Optional<E> optEntity = findById(dto);
      if (optEntity.isEmpty()) {
        throw new ServiceException(ErrorCode.data_validation_error, "Missing entity for DTO!",
          Set.of(new AttributeString(dto.identifierField(), dto.identifier())));
      }
      return optEntity.get();
    }

    return null;
  }
}
