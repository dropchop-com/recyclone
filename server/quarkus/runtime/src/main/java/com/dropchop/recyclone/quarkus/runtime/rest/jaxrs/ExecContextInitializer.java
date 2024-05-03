package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Constants;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;

import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_REQUEST_PATH;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 3. 05. 24.
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
    dataClass = dataClassName != null ? (Class<? extends Dto>) loadClass(dataClassName) : null;
    log.info("Registering REST initializer for [{}#{}] with [{}, {}, {}]",
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
    requestContext.setProperty(Constants.InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER, execContext);
  }
}
