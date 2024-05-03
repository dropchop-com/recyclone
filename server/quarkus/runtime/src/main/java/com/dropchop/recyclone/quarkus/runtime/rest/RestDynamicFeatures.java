package com.dropchop.recyclone.quarkus.runtime.rest;

import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextParamsBinder;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextSelector;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@SuppressWarnings({"CdiInjectionPointsInspection", "unused"})
public class RestDynamicFeatures implements DynamicFeature {

  private static final Logger log = LoggerFactory.getLogger(RestDynamicFeatures.class);

  @Inject
  RestMapping restMapping;

  @Inject
  ExecContextSelector execContextSelector;

  @Inject
  ExecContextParamsBinder execContextParamsBinder;

  public static String createMethodDescriptor(Method method) {
    Class<?>[] paramTypes = method.getParameterTypes();
    StringBuilder builder = new StringBuilder();
    builder.append(method.getDeclaringClass().getName());
    builder.append(".");
    builder.append(method.getName());
    builder.append("(");
    for (int i = 0; i < paramTypes.length; i++) {
      builder.append(paramTypes[i].getName());
      if (i < paramTypes.length - 1) {
        builder.append(",");
      }
    }
    builder.append(")");
    return builder.toString();
  }

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
    Class<?> riClass = resourceInfo.getResourceClass();
    Method method = resourceInfo.getResourceMethod();
    String methodKey = createMethodDescriptor(method);
    log.info("Registering REST API for [{}][{}]", methodKey, restMapping.getApiMethod(methodKey));
  }
}
