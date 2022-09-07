package com.dropchop.recyclone.model.api.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ParamsExecContextProvider extends ExecContextProvider {

  default  <P extends Params> void setParams(P params) {
    ExecContext<?> execContext = produce();
    if (params.getRequestId() != null) {
      execContext.setId(params.getRequestId());
    } else {
      params.setRequestId(execContext.getId());
    }
    if (execContext instanceof ParamsExecContext<?> paramsExecContext) {
      paramsExecContext.setParams(params);
    }
  }
}
