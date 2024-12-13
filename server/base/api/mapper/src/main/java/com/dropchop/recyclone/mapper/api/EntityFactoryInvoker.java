package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Context;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 05. 22.
 */
@Slf4j
@ApplicationScoped
public class EntityFactoryInvoker {

  @ObjectFactory
  @SuppressWarnings("unchecked")
  public <E extends Entity, D extends Dto> E create(D dto,
                                                    @Context MappingContext context,
                                                    @TargetType Class<E> type) {
    try {
      for (MappingListener listener : context.listeners()) {
        if (listener instanceof EntityFactoryListener<?, ?>) {
          Class<E> entityType = ((EntityFactoryListener<D, E>) listener).getEntityType();
          if (entityType.equals(type)) {
            E entity = ((EntityFactoryListener<D, E>) listener).create(dto, context);
            if (entity != null) {
              return entity;
            }
          }
        }
      }
      return type.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      log.error("[{}] Unable to instantiate [{}]", ErrorCode.internal_error, type, e);
      throw new ServiceException(ErrorCode.internal_error,
        String.format("Unable to instantiate [%s]!", type),
          Set.of(new AttributeString("class", type.getName())), e);
    }
  }
}
