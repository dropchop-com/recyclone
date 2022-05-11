package com.dropchop.recyclone.rest.jaxrs.server;

import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.CommonExecContextConsumer;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
public class ExecContextWriteInterceptor implements WriterInterceptor {

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
    Object entity = context.getEntity();
    log.trace("[{}].aroundWriteTo [{}].", this.getClass().getSimpleName(), entity);
    if (entity == null) {
      context.proceed();
      return;
    }

    CommonExecContext<?, ?> execContext = CommonExecContextConsumer.provider.get();
    if (execContext == null) {
      context.proceed();
      return;
    }
    if (!(entity instanceof Result)) {
      context.proceed();
      return;
    }
    log.trace("[{}].aroundWriteTo done.", this.getClass().getSimpleName());
    ((Result<?>) entity).setId(execContext.getId());
    ((Result<?>) entity).getStatus().setTime(execContext.getExecTime());
    context.proceed();
  }
}
