package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.mapper.api.MappingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Context;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;

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
      Class<?> tmp = mapperSubTypeConfig.mapsTo(model.getClass());
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
