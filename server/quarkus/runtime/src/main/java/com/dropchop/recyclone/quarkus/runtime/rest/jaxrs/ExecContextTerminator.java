package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.base.api.model.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.WriterInterceptor;
import jakarta.ws.rs.ext.WriterInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 05. 24.
 */
public class ExecContextTerminator implements WriterInterceptor {

  private static final Logger log = LoggerFactory.getLogger(ExecContextTerminator.class);

  private void clearMdc() {
    MDC.remove(MDC_REQUEST_ID);
    MDC.remove(MDC_SHORT_REQUEST_ID);
    MDC.remove(MDC_REQUEST_PATH);
    MDC.remove(MDC_SHORT_REQUEST_PATH);
    MDC.remove(MDC_PERSON_ID);
    MDC.remove(MDC_SHORT_PERSON_ID);
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

    ExecContext<?> execContext = (ExecContext<?>)context
        .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);

    if (execContext == null) {
      clearMdc();
      context.proceed();
      return;
    }
    if (!(entity instanceof Result)) {
      clearMdc();
      context.proceed();
      return;
    }
    log.trace("[{}].aroundWriteTo done.", this.getClass().getSimpleName());
    ((Result<?>) entity).setId(execContext.getId());
    ((Result<?>) entity).getStatus().setTime(execContext.getExecTime());
    context.proceed();
    clearMdc();
  }
}
