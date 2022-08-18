package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.DefaultExecContext;
import com.dropchop.recyclone.service.api.ExecContextType;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.core.UriInfo;

/**
 * Container and CDI provider for Request scoped {@link com.dropchop.recyclone.model.api.invoke.ExecContext}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 24. 05. 22.
 */
@Slf4j
@RequestScoped
@ExecContextType(Constants.Implementation.RCYN_DEFAULT)
public class DefaultExecContextProvider implements ExecContextProvider, ParamsExecContextProvider {

  DefaultExecContext<?> execContext;

  @Override
  @SuppressWarnings("rawtypes")
  public Class<? extends ExecContext> getContextClass() {
    return DefaultExecContext.class;
  }

  @Override
  public <D extends Dto> DefaultExecContext<D> create(UriInfo uriInfo) {
    this.execContext = new DefaultExecContext<>();
    log.debug("Created [{}] [{}]", this, this.execContext);
    //noinspection unchecked
    return (DefaultExecContext<D>) this.execContext;
  }

  @Produces
  @RequestScoped
  public <D extends Dto> DefaultExecContext<D> produce() {
    //noinspection unchecked
    return (DefaultExecContext<D>) this.execContext;
  }
}
