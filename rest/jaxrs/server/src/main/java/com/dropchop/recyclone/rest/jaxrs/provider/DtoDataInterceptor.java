package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.DataExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextProvider;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This ReaderInterceptor is to intercept methods with Dto data to add them to JAX-RS context internal variable.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 01. 22.
 */
@Slf4j
@ConstrainedTo(RuntimeType.SERVER)
public class DtoDataInterceptor implements ReaderInterceptor {

  private final Class<? extends Dto> dtoClass;

  public <D extends Dto> DtoDataInterceptor(Class<D> dtoClass) {
    this.dtoClass = dtoClass;
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

    if (o != null && this.dtoClass.isAssignableFrom(o.getClass())) {
      log.debug("Intercept [{}].", o);
      context.setProperty(InternalContextVariables.RECYCLONE_DATA, List.of((Dto)o));
    }
    ExecContext<?> execContext = execContextProvider.produce();
    if (execContext instanceof DataExecContext<?, ?> dataExecContext) {
      if (o != null && List.class.isAssignableFrom(o.getClass())) {
        if (((List<?>) o).iterator().hasNext()) {
          Object item = ((List<?>) o).iterator().next();
          if (item instanceof Dto) {
            //noinspection unchecked,rawtypes
            dataExecContext.setData((List) o);
            //context.setProperty(InternalContextVariables.RECYCLONE_DATA, o);
            log.debug("Intercept added collection of [{}].", item.getClass().getSimpleName());
          } else {
            dataExecContext.setData(new ArrayList<>());
            //context.setProperty(InternalContextVariables.RECYCLONE_DATA, new ArrayList<>());
            log.warn("Skip add data to [{}] since data does not contain [{}] instance!",
              dataExecContext.getClass().getSimpleName(), Dto.class.getSimpleName());
          }
        } else {
          dataExecContext.setData(new ArrayList<>());
          //context.setProperty(InternalContextVariables.RECYCLONE_DATA, new ArrayList<>());
        }
      }
    }
    return o;
  }
}
