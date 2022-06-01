package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.ExecContextProvider;
import com.dropchop.recyclone.service.api.ExecContextType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.Priorities;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

/**
 * WriterInterceptor that supports execution context's execution time and
 * request id {@link Result} response injection.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
@Provider
@Priority(Priorities.USER)
@ConstrainedTo(RuntimeType.SERVER)
public class ExecContextWriteInterceptor implements WriterInterceptor {

  @Inject
  @RequestScoped
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  ExecContextProvider execContextProvider;

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
    Object entity = context.getEntity();
    log.trace("[{}].aroundWriteTo [{}].", this.getClass().getSimpleName(), entity);
    if (entity == null) {
      context.proceed();
      return;
    }

    if (execContextProvider == null) {
      context.proceed();
      return;
    }
    if (!(entity instanceof Result)) {
      context.proceed();
      return;
    }
    log.debug("[{}].aroundWriteTo done.", this.getClass().getSimpleName());
    ((Result<?>) entity).setId(execContextProvider.get().getId());
    ((Result<?>) entity).getStatus().setTime(execContextProvider.get().getExecTime());
    context.proceed();
  }
}
