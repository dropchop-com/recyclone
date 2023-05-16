package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.utils.Uuid;
import com.dropchop.recyclone.model.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;

import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_REQUEST_ID;

/**
 * This is to intercept methods with CommonParams to context property for further processing.
 * Similar as {@link ParamsFactoryFilter} only this time parameters are already deserialized since they
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
      if (execContextProvider instanceof ParamsExecContextProvider paramsExecContextProvider) {
        Params p = (Params)o;
        String reqId = p.getRequestId();
        if (reqId == null || reqId.isBlank()) {
          p.setRequestId(Uuid.getTimeBased().toString());
        }
        MDC.put(MDC_REQUEST_ID, p.getRequestId());
        paramsExecContextProvider.setParams(p);
        context.setProperty(InternalContextVariables.RECYCLONE_PARAMS, p);
      }
    }
    return o;
  }
}
