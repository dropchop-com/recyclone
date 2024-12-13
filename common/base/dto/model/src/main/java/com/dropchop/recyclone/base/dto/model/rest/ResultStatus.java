package com.dropchop.recyclone.base.dto.model.rest;

import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuppressWarnings("unused")
public class ResultStatus implements com.dropchop.recyclone.base.api.model.rest.ResultStatus<ResultStats> {

  @NonNull
  private ResultCode code;

  private long time;

  private long total = 0;

  @JsonInclude(NON_NULL)
  private ResultStats stats;

  @JsonInclude(NON_NULL)
  private StatusMessage message;

  @JsonInclude(NON_NULL)
  private List<StatusMessage> details;

  public ResultStatus(@NonNull ResultCode code, long time, long total) {
    this.code = code;
    this.time = time;
    this.total = total;
  }
}
