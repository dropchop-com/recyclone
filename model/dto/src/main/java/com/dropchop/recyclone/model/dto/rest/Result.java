package com.dropchop.recyclone.model.dto.rest;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContext;
import com.dropchop.recyclone.model.api.rest.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 12. 21.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class Result<T> implements com.dropchop.recyclone.model.api.rest.Result<T, ResultStatus, ResultStats> {

  @NonNull
  @JsonInclude(NON_NULL)
  private String id;

  @NonNull
  @EqualsAndHashCode.Exclude
  private ResultStatus status = new ResultStatus();

  @NonNull
  @EqualsAndHashCode.Exclude
  private List<T> data = new ArrayList<>();


  public Result<T> toSuccess(List<T> data, int totalCount, ExecContext<?> context, ResultStats stats) {

    long time = 0;
    if (context instanceof ParamsExecContext<?> paramsExecContext) {
      time = paramsExecContext.getExecTime();
    }
    ResultStatus status = new ResultStatus(ResultCode.success, time, totalCount, stats, null, null);
    this.setStatus(status);

    return this;
  }

  public Result<T> toSuccess(List<T> data, int totalCount, ExecContext<?> context) {
    return toSuccess(data, totalCount, context, null);
  }

  public Result<T> toSuccess(List<T> data, int totalCount) {
    return toSuccess(data, totalCount, null, null);
  }

  public Result<T> toSuccess(List<T> data) {
    return toSuccess(data, 0, null, null);
  }

  public Result<T> toSuccess(List<T> data, ExecContext<?> context, ResultStats stats) {
    return toSuccess(data, 0, context, stats);
  }

  public Result<T> toSuccess(List<T> data, ResultStats stats) {
    return toSuccess(data, 0, null, stats);
  }
}
