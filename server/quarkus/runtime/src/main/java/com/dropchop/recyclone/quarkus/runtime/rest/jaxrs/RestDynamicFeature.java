package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static jakarta.ws.rs.Priorities.*;

@SuppressWarnings({"CdiInjectionPointsInspection", "unused"})
public class RestDynamicFeature implements DynamicFeature {

  private static final Logger log = LoggerFactory.getLogger(RestDynamicFeature.class);

  @Inject
  RestMapping restMapping;

  @Inject
  ExecContextBinder execContextBinder;

  public RestDynamicFeature() {
    log.info("Creating RestDynamicFeature");
  }

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
    Method method = resourceInfo.getResourceMethod();
    String methodDescriptor = method.toString();
    if (!method.getDeclaringClass().equals(resourceInfo.getResourceClass())) {
      methodDescriptor = methodDescriptor.replace(
          method.getDeclaringClass().getName(), resourceInfo.getResourceClass().getName()
      );
    }
    RestMethod restMethod = restMapping.getMethod(methodDescriptor);
    if (restMethod == null) {
      return;
    }
    if (restMethod.isExcluded()) {
      // TODO add blocking deny-all filter
      return;
    }

    log.trace("Looking at [{}#{}]", resourceInfo.getResourceClass().getName(), method.getName());
    RestClass restClass = restMapping.getApiClass(restMethod.getApiClass());

    // Input chain
    featureContext.register(
        new ExecContextInitializer( // Load ExecContext and Params from CDI and bind them
            restClass,
            restMethod,
            "com.dropchop.recyclone.model.dto.invoke.Params",
            "com.dropchop.recyclone.model.dto.invoke.DefaultExecContext",
            execContextBinder
        ),
        AUTHENTICATION - 100
    );

    if (restMethod.getAction().equals(RestMethod.Action.READ)) {
      featureContext.register( // initialize and pass new params to JAX-RS context property
          new ParamsDecoratorFilter(restClass, restMethod), HEADER_DECORATOR
      );
    }

    // Output chain
    featureContext.register( // terminate and fill result vars from exec context and clear MDC
        new ExecContextTerminator(),
        USER
    );
  }
}
