package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.Dto;
import com.dropchop.recyclone.model.api.Entity;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.rest.ResultCode;
import com.dropchop.recyclone.model.dto.rest.ResultStatus;
import org.mapstruct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 04. 22.
 */
public interface ToDtoMapper<D extends Dto, P extends Params, E extends Entity> {

  Logger log = LoggerFactory.getLogger(ToDtoMapper.class);

  interface DtoSupplier<D, E> {
    D get(E entity);
  }

  D toDto(E entity, @Context MappingContext<P> context);

  default List<D> toDtos(List<E> entities, MappingContext<P> context) {
    List<D> dtos = new ArrayList<>(entities.size());
    for (E entity : entities) {
      dtos.add(toDto(entity, context));
    }
    return dtos;
  }

  default Result<D> toDtosResult(List<E> entities, MappingContext<P> context, Supplier<ResultStatus> statusSupplier) {
    List<D> dtos = toDtos(entities, context);
    Result<D> objResult = new Result<>();
    if (dtos != null) {
      objResult.getData().addAll(dtos);
    }

    if (context.getTotalCount() <= 0) {
      context.setTotalCount(entities.size());
    }

    ResultStatus status = new ResultStatus(ResultCode.success, 0, context.getTotalCount(), null, null, null);
    if (statusSupplier != null) {
      ResultStatus tmp = statusSupplier.get();
      if (tmp != null) {
        status = tmp;
      }
    }

    objResult.setStatus(status);
    return objResult;
  }

  default Result<D> toDtosResult(List<E> entities, MappingContext<P> context) {
    return toDtosResult(entities, context, null);
  }

  D updateDto(E entity, @MappingTarget D dto, @Context MappingContext<P> context);

  default List<D> updateDtos(List<E> entities, @Context DtoSupplier<D, E> supplier, @Context MappingContext<P> context) {
    List<D> dtos = new ArrayList<>(entities.size());
    for (E entity : entities) {
      D dto = supplier.get(entity);
      if (dto == null) {
        throw new ServiceException(ErrorCode.not_found_error, "No such entity.",
          Set.of(new AttributeString(entity.identifierField(), entity.identifier())));
      }
      dtos.add(updateDto(entity, dto, context));
    }
    context.setTotalCount(entities.size());
    return dtos;
  }

  @BeforeMapping
  default void before(Object source, @MappingTarget Object target, @Context MappingContext<P> context) {
    if (context instanceof FilteringDtoContext<P>) {
      ((FilteringDtoContext<P>) context).before(source, target);
    }
    if (source instanceof Entity && target instanceof Dto) {
      for (MappingListener<P> listener : context.listeners()) {
        if (listener instanceof BeforeToDtoListener<P>) {
          ((BeforeToDtoListener<P>) listener).before((Entity) source, (Dto) target, context);
        }
      }
    }
  }

  @AfterMapping
  default void after(Object source, @MappingTarget Object target, @Context MappingContext<P> context) {
    if (source instanceof Entity && target instanceof Dto) {
      for (MappingListener<P> listener : context.listeners()) {
        if (listener instanceof AfterToDtoListener) {
          ((AfterToDtoListener<P>) listener).after((Entity) source, (Dto) target, context);
        }
      }
    }
    if (context instanceof FilteringDtoContext<P>) {
      ((FilteringDtoContext<P>) context).after(source, target);
    }
  }

  @Condition
  default boolean filter(@TargetProperty String propName, @Context MappingContext<P> context) {
    if (context instanceof FilteringDtoContext<P>) {
      return ((FilteringDtoContext<P>) context).filter(propName);
    }
    return true;
  }
}
