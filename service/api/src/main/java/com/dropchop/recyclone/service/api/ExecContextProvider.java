package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 24. 05. 22.
 */
@Slf4j
@RequestScoped
public class ExecContextProvider {

  CommonExecContext<?, ?> execContext;

  public <P extends Params, D extends Dto> CommonExecContext<P, D> create() {
    this.execContext = new CommonExecContext<>();
    log.error("CREATED EXEC CONTEXT");
    //noinspection unchecked
    return (CommonExecContext<P, D>) this.execContext;
  }

  @Produces
  @RequestScoped
  public <P extends Params, D extends Dto> CommonExecContext<P, D> get() {
    log.error("RETRIEVED EXEC CONTEXT");
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
