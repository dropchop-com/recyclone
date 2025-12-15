package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.CommonExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.model.invoke.ParamsExecContext;
import com.dropchop.recyclone.base.dto.model.invoke.DefaultExecContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.MDC_REQUEST_ID;
import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.MDC_SHORT_REQUEST_ID;

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

  @Produces
  @RequestScoped
  public DefaultExecContext<com.dropchop.recyclone.base.dto.model.base.Dto> produceDefaultExecContext() {
    return new DefaultExecContext<>();
  }

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
      String execContextId = execContext.getId();
      if (execContextId != null) {
        MDC.put(MDC_REQUEST_ID, execContextId);
        String shortId = execContextId.length() > 8 ? execContextId.substring(0, 8) : execContextId;
        MDC.put(MDC_SHORT_REQUEST_ID, shortId);
      }
      log.debug("Created and bound params [{}] with execution context [{}].", params, execContext);
    } else {
      log.warn("Null execution context binding attempt.");
    }
  }

  public ExecContext<?> bind(Class<? extends ExecContext<?>> execContextClass,
                             Class<? extends Dto> dataClass,
                             Class<? extends Params> paramsClass) {
    ExecContext<?> execContext;
    if (dataClass == null) {
      throw new IllegalArgumentException(
          "Data class cannot be null for exec context class [" + execContextClass
              + "], because we can not request the correct ExecContext from the Container!"
      );
    } else {
      execContext = execContextSelector.select(
          execContextClass, dataClass
      );
    }
    if (paramsClass != null) {
      Params params = paramsSelector.select(paramsClass);
      bind(execContext, params);
    }
    return execContext;
  }
}
