package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Context;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

import javax.enterprise.context.ApplicationScoped;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 05. 22.
 */
@Slf4j
@ApplicationScoped
public class EntityFactoryInvoker {

  @ObjectFactory
  public <E extends Entity, D extends Dto, P extends Params> E create(D dto,
                                                                      @Context MappingContext<P> context,
                                                                      @TargetType Class<E> type) {
    try {
      for (MappingListener<P> listener : context.listeners()) {
        if (listener instanceof EntityFactoryListener<?, ?, ?>) {
          Class<E> entityType = ((EntityFactoryListener<D, E, P>) listener).getEntityType();
          if (entityType.equals(type)) {
            E entity = ((EntityFactoryListener<D, E, P>) listener).create(dto, context);
            if (entity != null) {
              return entity;
            }
          }
        }
      }
      return type.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new ServiceException(ErrorCode.internal_error,
        String.format("Unable to instantiate [%s]!", type),
          Set.of(new AttributeString("class", type.getName())), e);
    }
  }
}
