package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.mapper.api.FilteringDtoContext;
import com.dropchop.recyclone.mapper.api.MappingContext;
import org.mapstruct.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 02. 24.
 */
@Mapper
public interface ToDtoManipulator {

  @BeforeMapping
  default void before(Object source, @MappingTarget Object target, @Context MappingContext context) {
    if (context instanceof FilteringDtoContext) {
      ((FilteringDtoContext) context).before(source, target);
    }
    if (source instanceof Model && target instanceof Dto) {
      for (MappingListener listener : context.listeners()) {
        if (listener instanceof BeforeToDtoListener) {
          ((BeforeToDtoListener) listener).before((Model) source, (Dto) target, context);
        }
      }
    }
  }

  @AfterMapping
  default void after(Object source, @MappingTarget Object target, @Context MappingContext context) {
    if (source instanceof Model && target instanceof Dto) {
      for (MappingListener listener : context.listeners()) {
        if (listener instanceof AfterToDtoListener) {
          ((AfterToDtoListener) listener).after((Model) source, (Dto) target, context);
        }
      }
    }
    if (context instanceof FilteringDtoContext) {
      ((FilteringDtoContext) context).after(source, target);
    }
  }

  @Condition
  default boolean filter(@TargetPropertyName String propName, @Context MappingContext context) {
    if (context instanceof FilteringDtoContext) {
      return ((FilteringDtoContext) context).filter(propName);
    }
    return true;
  }
}
