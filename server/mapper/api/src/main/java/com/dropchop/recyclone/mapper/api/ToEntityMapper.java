package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.mapper.api.MappingContext;
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
public interface ToEntityMapper<M extends Model, E extends Entity> {

  Logger log = LoggerFactory.getLogger(ToEntityMapper.class);

  E toEntity(M dto, @Context MappingContext context);

  default List<E> toEntities(List<M> models, @Context MappingContext context) {
    if (models == null) {
      return null;
    }
    List<E> entities = new ArrayList<>(models.size());
    for (M dto : models) {
      entities.add(toEntity(dto, context));
    }
    return entities;
  }

  @BeforeMapping
  default void beforeToEntity(Model model, @MappingTarget Entity entity, @Context MappingContext context) {
    for (MappingListener listener : context.listeners()) {
      if (listener instanceof BeforeToEntityListener beforeListener) {
        beforeListener.before(model, entity, context);
      }
    }
  }

  @AfterMapping
  default void afterToEntity(Model model, @MappingTarget Entity entity, @Context MappingContext context) {
    for (MappingListener listener : context.listeners()) {
      if (listener instanceof AfterToEntityListener afterListener) {
        afterListener.after(model, entity, context);
      }
    }
  }
}
