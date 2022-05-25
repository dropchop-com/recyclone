package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.Params;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;

/**
 * This is to intercept methods with CommonParams to context property for further processing.
 * Similar as {@link ParamsFactoryFilter} only this time parameters are already serialized since they
 * are proper JAX-RS resource method parameter.
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
      context.setProperty(InternalContextVariables.RECYCLONE_PARAMS, o);
    }
    return o;
  }
}
