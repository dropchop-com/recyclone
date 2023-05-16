package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.api.invoke.ExecContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

import static com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import static com.dropchop.recyclone.model.api.invoke.ExecContext.*;

/**
 * WriterInterceptor that supports execution context's execution time and
 * request id {@link Result} response injection.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 02. 22.
 */
@Slf4j
@ConstrainedTo(RuntimeType.SERVER)
public class ExecContextWriteInterceptor implements WriterInterceptor {

  private void clearMdc() {
    MDC.remove(MDC_REQUEST_ID);
    MDC.remove(MDC_REQUEST_PATH);
    MDC.remove(MDC_PERSON_ID);
    MDC.remove(MDC_PERSON_NAME);
  }

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
    Object entity = context.getEntity();
    log.trace("[{}].aroundWriteTo [{}].", this.getClass().getSimpleName(), entity);
    if (entity == null) {
      clearMdc();
      context.proceed();
      return;
    }

    ExecContextProvider execContextProvider = (ExecContextProvider)context
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);

    if (execContextProvider == null) {
      clearMdc();
      context.proceed();
      return;
    }
    if (!(entity instanceof Result)) {
      clearMdc();
      context.proceed();
      return;
    }
    log.debug("[{}].aroundWriteTo done.", this.getClass().getSimpleName());
    ((Result<?>) entity).setId(execContextProvider.produce().getId());
    ((Result<?>) entity).getStatus().setTime(execContextProvider.produce().getExecTime());
    context.proceed();
    clearMdc();
  }
}
