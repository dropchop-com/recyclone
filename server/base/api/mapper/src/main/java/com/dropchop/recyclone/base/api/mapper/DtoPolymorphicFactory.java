package com.dropchop.recyclone.base.api.mapper;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Context;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

import java.util.Collection;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 25. 05. 22.
 */
@Slf4j
@ApplicationScoped
public class DtoPolymorphicFactory {

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  MapperSubTypeConfig mapperSubTypeConfig;

  @ObjectFactory
  @SuppressWarnings({"unchecked", "unused"})
  public <M extends Model, D extends Dto> D create(M model,
                                                   @Context MappingContext context,
                                                   @TargetType Class<D> type) {
    if (mapperSubTypeConfig != null) {
      Collection<Class<?>> tmp = mapperSubTypeConfig.mapsTo(model.getClass());
      if (tmp != null && !tmp.isEmpty()) {
        Class<?> dtoCls = tmp.iterator().next();
        log.trace("Will instantiate dto [{}] instead of [{}] for model class [{}] from polymorphic registry.",
            dtoCls, type, model.getClass());
        type = (Class<D>) dtoCls;
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
