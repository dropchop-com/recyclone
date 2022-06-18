package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.service.api.invoke.MappingContext;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public interface ToEntityMapper<D extends Dto, E extends Entity> {

  Logger log = LoggerFactory.getLogger(ToEntityMapper.class);

  E toEntity(D dto, @Context MappingContext context);

  default List<E> toEntities(List<D> dtos, @Context MappingContext context) {
    List<E> entities = new ArrayList<>(dtos.size());
    for (D dto : dtos) {
      entities.add(toEntity(dto, context));
    }
    return entities;
  }

  @BeforeMapping
  default void beforeToEntity(Dto dto, @MappingTarget Entity entity, @Context MappingContext context) {
    for (MappingListener listener : context.listeners()) {
      if (listener instanceof BeforeToEntityListener beforeListener) {
        beforeListener.before(dto, entity, context);
      }
    }
  }

  @AfterMapping
  default void afterToEntity(Dto dto, @MappingTarget Entity entity, @Context MappingContext context) {
    for (MappingListener listener : context.listeners()) {
      if (listener instanceof AfterToEntityListener afterListener) {
        afterListener.after(dto, entity, context);
      }
    }
  }
}
