package com.dropchop.recyclone.model.dto.rest;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.invoke.StatusMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 12. 21.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultStatus implements Model {

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
