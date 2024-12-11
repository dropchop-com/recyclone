package com.dropchop.recyclone.repo.api.mapper;

import com.dropchop.recyclone.mapper.api.MappingContext;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.api.security.Constants;
import com.dropchop.recyclone.repo.api.ReadRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 06. 22.
 */
@Slf4j
public class EntityPolymorphicCreateFactory<D extends Dto, E extends Entity, ID>
    extends EntityDelegateFactory<D, E, ID> {

  private final MapperSubTypeConfig mapperSubTypeConfig;
  private final Class<?> entityHierarchyRoot;

  public EntityPolymorphicCreateFactory(ReadRepository<E, ID> repository,
                                        MapperSubTypeConfig mapperSubTypeConfig) {
    super(repository);
    this.mapperSubTypeConfig = mapperSubTypeConfig;
    this.entityHierarchyRoot = mapperSubTypeConfig.getEntityRootMarkerFor(repository.getRootClass());
    super.forActionOnly(Constants.Actions.CREATE);
  }

  @Override
  public E create(D dto, MappingContext context) {
    Class<?> targetType = null;
    if (mapperSubTypeConfig != null) {
      Collection<Class<?>> types = mapperSubTypeConfig.mapsTo(dto.getClass());
      if (types != null && !types.isEmpty()) {
        for (Class<?> type : types) {
          if (entityHierarchyRoot.isAssignableFrom(type)) {
            targetType = type;
            break;
          }
        }
        if (targetType == null) {
          throw new ServiceException(
              ErrorCode.internal_error,
              String.format("Unable to find appropriate child of [%s] for [%s]!", entityHierarchyRoot, dto.getClass())
          );
        }
        log.debug(
            "Will instantiate entity [{}] for dto class [{}] from polymorphic registry.",
            targetType, dto.getClass()
        );
        try {
          //noinspection unchecked
          return ((Class<E>)targetType).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
          throw new ServiceException(
              ErrorCode.internal_error, String.format("Unable to instantiate [%s]!", targetType),
              Set.of(new AttributeString("class", targetType.getName())), e
          );
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
