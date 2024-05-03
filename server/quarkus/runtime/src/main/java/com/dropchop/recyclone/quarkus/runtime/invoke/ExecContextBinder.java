package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_REQUEST_ID;

@ApplicationScoped
public class ExecContextBinder {

  private final Logger log = LoggerFactory.getLogger(ExecContextBinder.class);

  @Inject
  ExecContextContainer execContextContainer;

  public void bind(ExecContext<?> execContext, Params params) {
    if (params != null) {
      if (params.getRequestId() != null) {
        execContext.setId(params.getRequestId());
      } else {
        params.setRequestId(execContext.getId());
      }
      if (execContext instanceof ParamsExecContext<?> paramsExecContext) {
        paramsExecContext.setParams(params);
      }
    }
    execContextContainer.set(execContext);
    MDC.put(MDC_REQUEST_ID, execContext.getId());
    log.debug("Created and bound params [{}] with execution context [{}].", params, execContext);
  }
}
