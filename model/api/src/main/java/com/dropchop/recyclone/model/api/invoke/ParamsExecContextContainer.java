package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ParamsExecContextContainer extends ExecContextContainer {

  default  <P extends Params> void setParams(P params) {
    ParamsExecContext<?> execContext = get();
    if (params.getRequestId() != null) {
      execContext.setId(params.getRequestId());
    } else {
      params.setRequestId(execContext.getId());
    }
    execContext.setParams(params);
  }

  @SuppressWarnings("unused")
  <D extends Dto> ParamsExecContext<?> createContext();

  @SuppressWarnings("unused")
  <D extends Dto> ParamsExecContext<?> get();
}
