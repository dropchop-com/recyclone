package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Context;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 25. 05. 22.
 */
@Slf4j
@ApplicationScoped
public class DtoPolymorphicFactory {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  PolymorphicRegistry polymorphicRegistry;

  @ObjectFactory
  @SuppressWarnings("unchecked")
  public <M extends Model, D extends Dto> D create(M model,
                                                   @Context MappingContext context,
                                                   @TargetType Class<D> type) {
    if (polymorphicRegistry != null) {
      Class<?> tmp = polymorphicRegistry.mapsTo(model.getClass());
      if (tmp != null) {
        log.debug("Will instantiate dto [{}] instead of [{}] for model class [{}] from polymorphic registry.",
          tmp, type, model.getClass());
        type = (Class<D>) tmp;
      }
    } else {
      log.warn("Polymorphic registry is missing please configure your application.");
    }

    try {
      return type.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new ServiceException(ErrorCode.internal_error,
        String.format("Unable to instantiate [%s]!", type),
          Set.of(new AttributeString("class", type.getName())), e);
    }
  }
}
