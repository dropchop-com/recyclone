package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.service.api.CommonExecContext;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is to intercept methods with Dto data to add them to thread local CommonExecContext var.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 01. 22.
 */
@Slf4j
public class DtoDataInterceptor implements ReaderInterceptor {

  private final Class<? extends Dto> dtoClass;

  public <D extends Dto> DtoDataInterceptor(Class<D> dtoClass) {
    log.debug("Construct [{}] [{}].", this.getClass().getSimpleName(), dtoClass);
    this.dtoClass = dtoClass;
  }

  @Override
  public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
    Object o = context.proceed();
    if (o != null && this.dtoClass.isAssignableFrom(o.getClass())) {
      log.debug("Intercept [{}].", o);
      context.setProperty(InternalContextVariables.RECYCLONE_DATA, List.of((Dto)o));
    }
    if (o != null && List.class.isAssignableFrom(o.getClass())) {
      if (((List<?>) o).iterator().hasNext()) {
        Object item = ((List<?>) o).iterator().next();
        if (item instanceof Dto) {
          context.setProperty(InternalContextVariables.RECYCLONE_DATA, o);
          log.debug("Intercept added collection of [{}].", item.getClass().getSimpleName());
        } else {
          context.setProperty(InternalContextVariables.RECYCLONE_DATA, new ArrayList<>());
          log.warn("Skip add data to [{}] since data does not contain [{}] instance!",
            CommonExecContext.class.getSimpleName(), Dto.class.getSimpleName());
        }
      } else {
        context.setProperty(InternalContextVariables.RECYCLONE_DATA, new ArrayList<>());
      }
    }
    return o;
  }
}
