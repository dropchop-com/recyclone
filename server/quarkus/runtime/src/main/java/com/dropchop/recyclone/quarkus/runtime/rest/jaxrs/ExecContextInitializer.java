package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.Constants;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import com.dropchop.recyclone.base.api.model.invoke.Params;
import com.dropchop.recyclone.base.api.model.invoke.ParamsExecContext;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.MDC_REQUEST_PATH;
import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.ReqAttributeNames.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 05. 24.
 */
public class ExecContextInitializer implements ContainerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(ExecContextInitializer.class);

  private final ExecContextBinder execContextBinder;

  private final Class<? extends ExecContext<?>> execContextClass;

  private final Class<? extends Params> paramsClass;

  private final Class<? extends Dto> dataClass;

  private static Class<?> loadClass(String className) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    Class<?> clazz;
    try {
      clazz = cl.loadClass(className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return clazz;
  }

  @SuppressWarnings("unchecked")
  public ExecContextInitializer(RestClass restClass,
                                RestMethod restMethod,
                                String defaultParamsClassName,
                                String defaultExecContextClassName,
                                ExecContextBinder execContextBinder) {
    this.execContextBinder = execContextBinder;

    Class<? extends ExecContext<?>> defaultExecContextClass =
        (Class<? extends ExecContext<?>>) loadClass(defaultExecContextClassName);
    Class<? extends Params> defaultParamsClass =
        (Class<? extends Params>) loadClass(defaultParamsClassName);

    String ctxClassName = restMethod.getContextClass();
    if (ctxClassName == null) {
      ctxClassName = restClass.getCtxClass();
    }
    execContextClass = ctxClassName != null ?
        (Class<? extends ExecContext<?>>) loadClass(ctxClassName) : defaultExecContextClass;

    String paramsClassName = restMethod.getParamClass();
    if (paramsClassName == null) {
      paramsClassName = restClass.getParamClass();
    }
    paramsClass = paramsClassName != null ?
        (Class<? extends Params>) loadClass(paramsClassName) : defaultParamsClass;

    String dataClassName = restMethod.getMethodDataClass();
    if (dataClassName == null) {
      dataClassName = restClass.getDataClass();
    }
    dataClass = dataClassName != null
        ? (Class<? extends Dto>) loadClass(dataClassName)
        : com.dropchop.recyclone.base.dto.model.base.Dto.class;
    log.debug("Registering REST initializer for [{}#{}] with [{}, {}, {}]",
        restClass.getImplClass(),
        restMethod.getName(),
        execContextClass,
        paramsClass,
        dataClass
        );
  }

  /**
   * This is the starting request initialization entry point
   */
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    MDC.put(MDC_REQUEST_PATH, requestContext.getUriInfo().getPath());
    ExecContext<?> execContext = execContextBinder.bind(execContextClass, dataClass, paramsClass);
    if (execContext instanceof HasAttributes hasAttributes) {
      Instance<HttpServerRequest> inst = CDI.current().select(HttpServerRequest.class);
      try {
        HttpServerRequest request = inst.get();
        String clientAddress = request.getHeader("X-Forwarded-For");
        String clientHost = null;
        if (clientAddress == null) {
          clientAddress = request.remoteAddress().hostAddress();
          clientHost = request.remoteAddress().host();
        }
        if (clientHost != null) {
          hasAttributes.setAttributeValue(REQ_CLIENT_HOST, clientHost);
        }
        if (clientAddress != null) {
          hasAttributes.setAttributeValue(REQ_CLIENT_ADDRESS, clientAddress);
        }
      } catch (ContextNotActiveException e) {
        log.warn("CDI context not active to obtain HttpServerRequest!", e);
      } catch (Exception e) {
        log.warn("CDI context not active!", e);
      }

      hasAttributes.setAttributeValue(REQ_METHOD, requestContext.getMethod());
      hasAttributes.setAttributeValue(REQ_PATH, requestContext.getUriInfo().getPath());
      hasAttributes.setAttributeValue(REQ_URI, requestContext.getUriInfo().getRequestUri().toString());
      hasAttributes.setAttributeValue(REQ_URI_HOST, requestContext.getUriInfo().getRequestUri().getHost());
      hasAttributes.setAttributeValue(REQ_URI_QUERY, requestContext.getUriInfo().getRequestUri().getQuery());
      hasAttributes.setAttributeValue(REQ_URI_FRAGMENT, requestContext.getUriInfo().getRequestUri().getFragment());
      hasAttributes.setAttributeValue(REQ_URI_SCHEME, requestContext.getUriInfo().getRequestUri().getScheme());
    }
    requestContext.setProperty(Constants.InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER, execContext);
    if (execContext instanceof ParamsExecContext<?> paramsExecContext) {
      Params p = paramsExecContext.tryGetParams();
      requestContext.setProperty(Constants.InternalContextVariables.RECYCLONE_PARAMS, p);
    }
  }
}
