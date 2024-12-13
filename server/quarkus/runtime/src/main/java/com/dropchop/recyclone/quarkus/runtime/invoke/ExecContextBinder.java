package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.model.invoke.ParamsExecContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.MDC_REQUEST_ID;

@ApplicationScoped
@SuppressWarnings("CdiInjectionPointsInspection")
public class ExecContextBinder {

  private final Logger log = LoggerFactory.getLogger(ExecContextBinder.class);

  @Inject
  ExecContextContainer execContextContainer;

  @Inject
  CommonExecContextContainer commonExecContextContainer;

  @Inject
  ParamsExecContextContainer paramsExecContextContainer;

  @Inject
  ExecContextSelector execContextSelector;

  @Inject
  ParamsSelector paramsSelector;

  public void bind(ExecContext<?> execContext, Params params) {
    if (params != null && execContext != null) {
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
    if (execContext instanceof CommonExecContext<?,?> commonExecContext) {
      commonExecContextContainer.set(commonExecContext);
    }
    if (execContext instanceof ParamsExecContext<?> paramsExecContext) {
      paramsExecContextContainer.set(paramsExecContext);
    }
    if (execContext != null) {
      MDC.put(MDC_REQUEST_ID, execContext.getId());
      log.debug("Created and bound params [{}] with execution context [{}].", params, execContext);
    } else {
      log.warn("Null execution context binding attempt.");
    }
  }

  public ExecContext<?> bind(Class<? extends ExecContext<?>> execContextClass,
                             Class<? extends Dto> dataClass,
                             Class<? extends Params> paramsClass) {
    ExecContext<?> execContext = execContextSelector.select(execContextClass, dataClass);
    if (paramsClass != null) {
      Params params = paramsSelector.select(paramsClass);
      bind(execContext, params);
    }
    return execContext;
  }
}
