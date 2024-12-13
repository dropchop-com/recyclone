package com.dropchop.recyclone.mapper.api;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.rest.ResultStatus;
import org.mapstruct.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 04. 22.
 */
public interface ToDtoMapper<D extends Dto, X extends Model> {

  Logger log = LoggerFactory.getLogger(ToDtoMapper.class);

  D toDto(X model, @Context MappingContext context);

  default List<D> toDtos(Collection<X> entities, MappingContext context) {
    List<D> dtos = new ArrayList<>(entities.size());
    for (X entity : entities) {
      dtos.add(toDto(entity, context));
    }
    return dtos;
  }

  default Result<D> toDtosResult(Collection<X> models, MappingContext context, Supplier<ResultStatus> statusSupplier) {
    List<D> dtos = toDtos(models, context);
    Result<D> objResult = new Result<>();
    if (dtos != null) {
      objResult.getData().addAll(dtos);
    }

    if (context.getTotalCount() <= 0) {
      context.setTotalCount(models.size());
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

  default Result<D> toDtosResult(Collection<X> models, MappingContext context) {
    return toDtosResult(models, context, null);
  }
}
