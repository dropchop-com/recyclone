package com.dropchop.recyclone.rest.jaxrs.server;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.service.api.CommonExecContextConsumer;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;

/**
 * This is to intercept methods with CommonParams to add them to thread local var
 * to be additionally decorated with CommonParamsFilter.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 01. 22.
 */
@Slf4j
public class ParamsInterceptor implements ReaderInterceptor {

  private final Class<? extends Params> parametersClass;

  public <P extends Params> ParamsInterceptor(Class<P> parametersClass) {
    log.debug("Construct [{}] [{}].", this.getClass().getSimpleName(), parametersClass);
    this.parametersClass = parametersClass;
  }

  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
    Object o = context.proceed();
    if (o != null && this.parametersClass.isAssignableFrom(o.getClass())) {
      log.debug("Intercept [{}].", o);
      CommonExecContextConsumer.provider.setParams((Params) o);
    }
    return o;
  }
}
