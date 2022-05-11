package com.dropchop.recyclone.rest.jaxrs.server;

import com.dropchop.recyclone.service.api.CommonExecContextConsumer;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

/**
 * Resets common context thread local variable.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
public class ExecContextDestroyInterceptor implements WriterInterceptor {

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
    log.trace("[{}]", this.getClass().getSimpleName());
    CommonExecContextConsumer.provider.destroy();
    context.proceed();
  }
}
