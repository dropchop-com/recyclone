package com.dropchop.recyclone.service.api;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 22.
 */
@Slf4j
@ApplicationScoped
public class CommonExecContextProvider {

  private static final ThreadLocal<CommonExecContext<? extends Params, ? extends Dto>> thContext = new ThreadLocal<>();

  public void destroy() {
    thContext.set(null);
  }

  public <P extends Params, D extends Dto> CommonExecContext<P, D> create() {
    thContext.set(new CommonExecContext<>());
    return get();
  }

  public <P extends Params> void setParams(P p) {
    if (p instanceof com.dropchop.recyclone.model.dto.invoke.Params) {
      com.dropchop.recyclone.model.dto.invoke.Params params = (com.dropchop.recyclone.model.dto.invoke.Params) p;
      @SuppressWarnings("unchecked")
      CommonExecContext<P, ?> execContext = (CommonExecContext<P, ?>)thContext.get();
      if (execContext == null) {
        return;
      }
      if (params.getRequestId() != null) {
        execContext.setId(params.getRequestId());
      } else {
        params.setRequestId(execContext.getId());
      }
      execContext.setParams(p);
    }
  }

  public <D extends Dto> void setData(List<D> data) {
    @SuppressWarnings("unchecked")
    CommonExecContext<?, D> execContext = (CommonExecContext<?, D>)thContext.get();
    if (execContext == null) {
      return;
    }
    execContext.setData(data);
  }

  public <P extends Params> P getParams() {
    //log.error("CommonExecContext producer method invoked!");
    //noinspection unchecked
    CommonExecContext<P, ?> execContext = (CommonExecContext<P, ?>)thContext.get();
    if (execContext == null) {
      return null;
    }
    return execContext.getParams();
  }

  public <D extends Dto> List<D> getData() {
    //log.error("CommonExecContext producer method invoked!");
    //noinspection unchecked
    CommonExecContext<?, D> execContext = (CommonExecContext<?, D>)thContext.get();
    if (execContext == null) {
      return null;
    }
    return execContext.getData();
  }


  @Produces
  @RequestScoped
  public <P extends Params, D extends Dto> CommonExecContext<P, D> get() {
    log.trace("CommonExecContext.get producer method invoked!");
    //noinspection unchecked
    return (CommonExecContext<P, D>)thContext.get();
  }
}
