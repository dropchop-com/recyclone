package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContext;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ParamsExecContextProvider extends ExecContextProvider {

  default  <P extends Params> void setParams(P p) {
    if (p instanceof com.dropchop.recyclone.model.dto.invoke.Params params) {
      ExecContext<?> execContext = produce();
      if (params.getRequestId() != null) {
        execContext.setId(params.getRequestId());
      } else {
        params.setRequestId(execContext.getId());
      }
      if (execContext instanceof ParamsExecContext<?> paramsExecContext) {
        paramsExecContext.setParams(p);
      }
    }
  }
}
