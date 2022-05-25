package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.marker.Constants;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

/**
 * Conatiner and CDI provider for Request scoped {@link com.dropchop.recyclone.model.api.invoke.ExecContext}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 24. 05. 22.
 */
@Slf4j
@RequestScoped
@ExecContextType(Constants.Implementation.RCYN_DEFAULT)
public class ExecContextProvider {

  CommonExecContext<?, ?> execContext;

  public <P extends Params, D extends Dto> CommonExecContext<P, D> create() {
    this.execContext = new CommonExecContext<>();
    //noinspection unchecked
    return (CommonExecContext<P, D>) this.execContext;
  }

  @Produces
  @RequestScoped
  public <P extends Params, D extends Dto> CommonExecContext<P, D> get() {
    //noinspection unchecked
    return (CommonExecContext<P, D>) this.execContext;
  }

  public <P extends Params> void setParams(P p) {
    if (p instanceof com.dropchop.recyclone.model.dto.invoke.Params params) {
      @SuppressWarnings("unchecked")
      CommonExecContext<P, ?> execContext = (CommonExecContext<P, ?>)this.execContext;
      if (params.getRequestId() != null) {
        execContext.setId(params.getRequestId());
      } else {
        params.setRequestId(execContext.getId());
      }
      execContext.setParams(p);
    }
  }
}
