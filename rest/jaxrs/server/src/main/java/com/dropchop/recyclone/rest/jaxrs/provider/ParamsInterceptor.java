package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.service.api.invoke.ExecContextProvider;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
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
@ConstrainedTo(RuntimeType.SERVER)
public class ParamsInterceptor implements ReaderInterceptor {

  private final Class<? extends Params> parametersClass;

  public <P extends Params> ParamsInterceptor(Class<P> parametersClass) {
    log.debug("Construct [{}] [{}].", this.getClass().getSimpleName(), parametersClass);
    this.parametersClass = parametersClass;
  }

  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
    Object o = context.proceed();
    ExecContextProvider execContextProvider = (ExecContextProvider)context
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContextProvider == null) {
      log.warn("Missing {} in {}!", ExecContextProvider.class.getSimpleName(), ReaderInterceptorContext.class.getSimpleName());
      return o;
    }
    if (o != null && this.parametersClass.isAssignableFrom(o.getClass())) {
      log.debug("Intercept [{}].", o);
      execContextProvider.setParams((CommonParams) o);
      //context.setProperty(InternalContextVariables.RECYCLONE_PARAMS, o);
    }
    return o;
  }
}
