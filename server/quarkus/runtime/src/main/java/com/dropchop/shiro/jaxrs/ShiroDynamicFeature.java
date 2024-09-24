package com.dropchop.shiro.jaxrs;

import com.dropchop.recyclone.model.api.security.annotations.*;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 12. 21.
 */
//@Provider
public class ShiroDynamicFeature implements DynamicFeature {

  private static final Logger log = LoggerFactory.getLogger(ShiroDynamicFeature.class);

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  RestMapping restMapping; // TODO: read sec spec in the processor.

  public ShiroDynamicFeature() {
    log.info("Creating ShiroDynamicFeature");
  }

  private static final List<Class<? extends Annotation>> shiroAnnotations =
    List.of(
      RequiresPermissions.class,
      RequiresRoles.class,
      RequiresAuthentication.class,
      RequiresUser.class,
      RequiresGuest.class
    );

  @Inject
  ShiroAuthorizationService authorizationService;

  private static <T extends Annotation> boolean checkAdd(final List<Annotation> authzSpecs,
                                                         final T[] annoArray) {
    boolean added = false;
    for (T anno : annoArray) {
      authzSpecs.add(anno);
      added = true;
    }
    return added;
  }


  public static <T extends Annotation> boolean findAnnotation(final Class<T> annotationType,
                                                              final List<Annotation> authzSpecs,
                                                              final Class<?> myClass,
                                                              final Method myMethod) {
    T[] annoArray = myMethod.getAnnotationsByType(annotationType);
    if (checkAdd(authzSpecs, annoArray)) {
      return true;
    }
    Class<?> declaringClass = myClass;
    if (myClass.equals(Object.class)) {
      return false;
    }
    annoArray = declaringClass.getAnnotationsByType(annotationType);
    if (checkAdd(authzSpecs, annoArray)) {
      return true;
    }
    for (Class<?> iface : declaringClass.getInterfaces()) {
      try {
        Method m = iface.getMethod(myMethod.getName(), myMethod.getParameterTypes());
        if (findAnnotation(annotationType, authzSpecs, iface, m)) {
          return true;
        }
      } catch (NoSuchMethodException ignored) {
      }
    }
    declaringClass = declaringClass.getSuperclass();
    if (declaringClass == null) {
      return false;
    }
    try {
      Method m = declaringClass.getMethod(myMethod.getName(), myMethod.getParameterTypes());
      if (findAnnotation(annotationType, authzSpecs, declaringClass, m)) {
        return true;
      }
    } catch (NoSuchMethodException ignored) {
    }

    return false;
  }

  @Override
  public void configure(ResourceInfo resourceInfo, FeatureContext context) {
    List<Annotation> authzSpecs = new ArrayList<>();
    Method method = resourceInfo.getResourceMethod();
    Class<?> clazz = resourceInfo.getResourceClass();
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

    for (Class<? extends Annotation> annotationClass : shiroAnnotations) {
      findAnnotation(annotationClass, authzSpecs, clazz, method);
    }

    context.register(
      new ShiroThreadStateFilter(authorizationService), Priorities.AUTHORIZATION - 100
    );

    if (!authzSpecs.isEmpty()) {
      context.register(
        new ShiroResponseFilter(authorizationService),
        Priorities.AUTHORIZATION
      );
      log.trace(
          "Registered {} for [{}:{}] with [{}]",
          ShiroResponseFilter.class.getSimpleName(),
          resourceInfo.getResourceClass().getSimpleName(),
          resourceInfo.getResourceMethod().getName(),
          authzSpecs
      );
      context.register(
        new ShiroAuthorizationFilter(
            authorizationService,
            authzSpecs,
            resourceInfo.getResourceClass().getSimpleName(),
            resourceInfo.getResourceMethod().getName()),
        Priorities.AUTHORIZATION
      );
      log.debug(
          "Registered {} for [{}:{}] with [{}]",
          ShiroAuthorizationFilter.class.getSimpleName(),
          resourceInfo.getResourceClass().getSimpleName(),
          resourceInfo.getResourceMethod().getName(),
          authzSpecs
      );
    }
  }
}
