package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.invoke.ErrorCode;
import com.dropchop.recyclone.model.api.invoke.ServiceException;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.rest.ResultCode;
import com.dropchop.recyclone.model.dto.rest.ResultStats;
import com.dropchop.recyclone.model.dto.rest.ResultStatus;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 12. 21.
 */
public class ResultWrapper<T> {

  public Result<T> wrap(Supplier<Collection<T>> supplier, Supplier<ResultStats> statsSupplier) {
    long start = System.currentTimeMillis();
    Collection<T> objects;
    try {
      objects = supplier.get();
    } catch (Exception e) {// pass to ExceptionMapper
      if (e instanceof ServiceException) {
        ((ServiceException) e).setFromWrappedCall(true);
        throw e;
      } else {
        ServiceException see = new ServiceException(ErrorCode.internal_error, e.getMessage());
        see.setFromWrappedCall(true);
        throw see;
      }
    }

    Result<T> objResult = new Result<>();
    if (objects != null) {
      objResult.getData().addAll(objects);
    }

    ResultStats stats = null;
    if (statsSupplier != null) {
      stats = statsSupplier.get();
    }

    objResult.setStatus(new ResultStatus(ResultCode.success, System.currentTimeMillis() - start, 0, stats, null, null));
    return objResult;
  }

  public Result<T> wrap(Supplier<Collection<T>> supplier) {
    return wrap(supplier, null);
  }
}
