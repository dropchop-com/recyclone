package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.service.api.EntityByIdService;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 21. 06. 22.
 */
@Slf4j
public class EntityPolymorphicCreateFactory<D extends Dto, E extends Entity, ID> extends EntityDelegateFactory<D, E, ID> {

  private final PolymorphicRegistry polymorphicRegistry;

  public EntityPolymorphicCreateFactory(EntityByIdService<D, E, ID> service, PolymorphicRegistry polymorphicRegistry) {
    super(service);
    this.polymorphicRegistry = polymorphicRegistry;
    super.forActionOnly(Constants.Actions.CREATE);
  }

  @Override
  public E create(D dto, MappingContext context) {
    if (polymorphicRegistry != null) {
      Class<?> type = polymorphicRegistry.mapsTo(dto.getClass());
      if (type != null) {
        log.debug("Will instantiate entity [{}] for dto class [{}] from polymorphic registry.",
          type, dto.getClass());
        try {
          //noinspection unchecked
          return ((Class<E>)type).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
          throw new ServiceException(ErrorCode.internal_error,
            String.format("Unable to instantiate [%s]!", type),
              Set.of(new AttributeString("class", type.getName())), e);
        }
      }
    } else {
      log.warn("Polymorphic registry is missing please configure your application.");
    }
    return null;
  }

  @Override
  public EntityDelegateFactory<D, E, ID> forActionOnly(String action) {
    throw new UnsupportedOperationException("Only for create action is supported!");
  }
}
