package com.dropchop.recyclone.rest.jaxrs.server;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.Dto;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.CommonExecContextConsumer;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 19. 01. 22.
 */
@Slf4j
public class ResultFieldFilterInterceptor implements WriterInterceptor {
  public interface ShouldNull {
    boolean doNull(String targetFieldName);
  }

  public void nullFields(Object o, ShouldNull shouldNull) {
    BeanInfo info;
    try {
      info = Introspector.getBeanInfo(o.getClass());
    } catch (Exception e) {
      log.error("Unable to introspect [{}]!", o);
      return;
    }

    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
      if (pd.getReadMethod() == null) {
        continue;
      }
      if (!shouldNull.doNull(pd.getName())) {
        continue;
      }
      Method m = pd.getWriteMethod();
      if (m == null) {
        continue;
      }
      try {
        m.invoke(o, new Object[]{null});
      } catch (Exception e) {
        log.error("Unable to null filed [{}] on [{}] with setter [{}]", pd.getName(), o, m.getName(), e);
      }
    }
  }

  @Override
  public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {

    Params parameters = CommonExecContextConsumer.provider.getParams();
    if (parameters == null) {
      context.proceed();
      return;
    }

    List<String> excludes = parameters.getContentExcludes();
    List<String> includes = parameters.getContentIncludes();

    if ((excludes == null || excludes.isEmpty()) && (includes == null || includes.isEmpty())) {
      context.proceed();
      return;
    }

    Object entity = context.getEntity();
    if (entity == null) {
      context.proceed();
      return;
    }

    if (includes != null && !includes.isEmpty()) {
      log.info("Will include only [{}].", includes);
      if (entity instanceof Dto) {
        nullFields(entity, (targetFieldName) -> !includes.contains(targetFieldName));
      } else if (entity instanceof Result) {
        List<?> data = ((Result<?>) entity).getData();
        for (Object o : data) {
          nullFields(o, (targetFieldName) -> !includes.contains(targetFieldName));
        }
      } else if (entity instanceof Collection) {
        for (Object o : (Collection<?>) entity) {
          nullFields(o, (targetFieldName) -> !includes.contains(targetFieldName));
        }
      }
    }

    if (excludes != null && !excludes.isEmpty()) {
      log.info("Will exclude [{}].", excludes);
      if (entity instanceof Dto) {
        nullFields(entity, excludes::contains);
      } else if (entity instanceof Result) {
        List<?> data = ((Result<?>) entity).getData();
        for (Object o : data) {
          nullFields(o, excludes::contains);
        }
      } else if (entity instanceof Collection) {
        for (Object o : (Collection<?>) entity) {
          nullFields(o, excludes::contains);
        }
      }
    }

    context.proceed();
  }
}
