package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.base.api.model.invoke.DataExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This ReaderInterceptor is to intercept methods with Dto data to add them to JAX-RS context internal variable.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 01. 22.
 */
@ConstrainedTo(RuntimeType.SERVER)
public class DtoDataInterceptor implements ReaderInterceptor {

  private final Logger log = LoggerFactory.getLogger(DtoDataInterceptor.class);
  private final Class<?> dtoClass;

  public DtoDataInterceptor(RestClass restClass, RestMethod restMethod) {
    String dataClassName = restMethod.getMethodDataClass();
    if (dataClassName == null) {
      dataClassName = restClass.getDataClass();
    }
    Class<?> dataClass;
    if (dataClassName == null) {
      dataClass = null;
    } else {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      try {
        dataClass = cl.loadClass(dataClassName);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    this.dtoClass = dataClass;
  }

  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
    Object o = context.proceed();
    if (dtoClass == null) {
      return o;
    }
    ExecContext<?> execContext = (ExecContext<?>)context
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContext == null) {
      log.warn("Missing {} in {}!", ExecContextContainer.class.getSimpleName(), ReaderInterceptorContext.class.getSimpleName());
      return o;
    }

    if (o != null && this.dtoClass.isAssignableFrom(o.getClass())) {
      log.debug("Intercept [{}].", o);
      //context.setProperty(InternalContextVariables.RECYCLONE_DATA, List.of((Dto)o));
    }
    if (execContext instanceof DataExecContext<?, ?> dataExecContext) {
      if (o != null && List.class.isAssignableFrom(o.getClass())) {
        if (((List<?>) o).iterator().hasNext()) {
          Object item = ((List<?>) o).getFirst();
          if (item instanceof Dto) {
            //noinspection unchecked,rawtypes
            dataExecContext.setData((List) o);
            log.debug("Intercept added collection of [{}].", item.getClass().getSimpleName());
          } else {
            dataExecContext.setData(new ArrayList<>());
            log.warn("Skip add data to [{}] since data does not contain [{}] instance!",
              dataExecContext.getClass().getSimpleName(), Dto.class.getSimpleName());
          }
        } else {
          dataExecContext.setData(new ArrayList<>());
        }
      }
    }
    return o;
  }
}
