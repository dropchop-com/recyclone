package com.dropchop.recyclone.base.api.repo.mapper;

import com.dropchop.recyclone.base.api.mapper.EntityFactoryListener;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.security.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 06. 22.
 */
@Slf4j
public class EntityPolymorphicCreateFactory<D extends Dto, E extends Entity>
    extends EntityDelegate<D, E> implements EntityFactoryListener<D, E> {

  private final MapperSubTypeConfig mapperSubTypeConfig;
  private final Collection<Class<?>> entityHierarchyRoots;

  public <X extends Entity> EntityPolymorphicCreateFactory(MapperSubTypeConfig mapperSubTypeConfig,
                                                           Collection<Class<X>> rootClasses) {
    super(rootClasses);
    this.mapperSubTypeConfig = mapperSubTypeConfig;
    this.entityHierarchyRoots = new HashSet<>();
    for (Class<?> rootClass : rootClasses) {
      Class<?> entityHierarchyRoot = mapperSubTypeConfig.getEntityRootMarkerFor(rootClass);
      if (entityHierarchyRoot == null) {
        throw new RuntimeException("Missing mapped root marker for [" + rootClass + "]");
      }
      this.entityHierarchyRoots.add(entityHierarchyRoot);
    }
    super.forActionOnly(Constants.Actions.CREATE);
  }

  public EntityPolymorphicCreateFactory(MapperSubTypeConfig mapperSubTypeConfig, Class<E> rootClass) {
    this(mapperSubTypeConfig, Set.of(rootClass));
  }

  @Override
  public E create(D dto, MappingContext context) {
    Class<?> targetType = null;
    if (mapperSubTypeConfig != null) {
      Collection<Class<?>> types = mapperSubTypeConfig.mapsTo(dto.getClass());
      if (types != null && !types.isEmpty()) {
        for (Class<?> type : types) {
          for (Class<?> rootType : entityHierarchyRoots) {
            if (rootType.isAssignableFrom(type)) {
              targetType = type;
              break;
            }
          }
          if (targetType != null) {
            break;
          }
        }
        if (targetType == null) {
          throw new ServiceException(
              ErrorCode.internal_error,
              String.format("Unable to find appropriate child of [%s] for [%s]!", entityHierarchyRoots, dto.getClass())
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
  public EntityPolymorphicCreateFactory<D, E> forActionOnly(String action) {
    throw new UnsupportedOperationException("Only for create action is supported!");
  }
}
