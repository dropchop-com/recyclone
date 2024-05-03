package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextSelector;
import com.dropchop.recyclone.quarkus.runtime.invoke.ParamsSelector;
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

import static jakarta.ws.rs.Priorities.AUTHENTICATION;

@SuppressWarnings({"CdiInjectionPointsInspection", "unused"})
public class RestDynamicFeatures implements DynamicFeature {

  private static final Logger log = LoggerFactory.getLogger(RestDynamicFeatures.class);

  @Inject
  RestMapping restMapping;

  @Inject
  ExecContextSelector execContextSelector;

  @Inject
  ParamsSelector paramsSelector;

  @Inject
  ExecContextBinder execContextBinder;

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
    Class<?> riClass = resourceInfo.getResourceClass();
    Method method = resourceInfo.getResourceMethod();
    RestMethod restMethod = restMapping.getMethod(method.toString());
    if (restMethod == null) {
      return;
    }
    if (restMethod.isExcluded()) {
      //TODO add blocking deny-all filter
      return;
    }
    RestClass restClass = restMapping.getApiClass(restMethod.getApiClass());
    featureContext.register(
        new ExecContextInitializer(
            restClass,
            restMethod,
            "com.dropchop.recyclone.model.dto.invoke.Params",
            "com.dropchop.recyclone.model.dto.invoke.DefaultExecContext",
            execContextSelector,
            paramsSelector,
            execContextBinder
        ),
        AUTHENTICATION
    );
  }
}
