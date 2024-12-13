package com.dropchop.recyclone.model.dto.rest;

import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 12. 21.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class Result<T> implements com.dropchop.recyclone.base.api.model.rest.Result<T, ResultStatus, ResultStats> {

  @NonNull
  @JsonInclude(NON_NULL)
  private String id;

  @NonNull
  @EqualsAndHashCode.Exclude
  private ResultStatus status = new ResultStatus();

  @NonNull
  @EqualsAndHashCode.Exclude
  private List<T> data = new ArrayList<>();


  public Result<T> toSuccess(List<T> data, int totalCount, ResultStats stats) {

    ResultStatus status = new ResultStatus(ResultCode.success, 0, totalCount, stats, null, null);
    this.setStatus(status);
    this.setData(data);
    return this;
  }

  public Result<T> toSuccess(List<T> data, int totalCount) {
    return toSuccess(data, totalCount, null);
  }

  public Result<T> toSuccess(List<T> data) {
    return toSuccess(data, 0, null);
  }

  public Result<T> toSuccess(List<T> data, ResultStats stats) {
    return toSuccess(data, 0, stats);
  }
}
