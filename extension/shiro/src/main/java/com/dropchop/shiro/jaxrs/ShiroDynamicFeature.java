package com.dropchop.shiro.jaxrs;

import com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
@Provider
public class ShiroDynamicFeature implements DynamicFeature {

  private static final List<Class<? extends Annotation>> shiroAnnotations =
    List.of(
      RequiresPermissions.class
      /*RequiresRoles.class,
      RequiresAuthentication.class,
      RequiresUser.class,
      RequiresGuest.class*/
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
                                                              final Method myMethod) {
    T[] annoArray = myMethod.getAnnotationsByType(annotationType);
    if (checkAdd(authzSpecs, annoArray)) {
      return true;
    }
    Class<?> declaringClass = myMethod.getDeclaringClass();
    if (declaringClass.equals(Object.class)) {
      return false;
    }
    annoArray = declaringClass.getAnnotationsByType(annotationType);
    if (checkAdd(authzSpecs, annoArray)) {
      return true;
    }
    for (Class<?> iface : declaringClass.getInterfaces()) {
      try {
        Method m = iface.getMethod(myMethod.getName(), myMethod.getParameterTypes());
        if (findAnnotation(annotationType, authzSpecs, m)) {
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
      if (findAnnotation(annotationType, authzSpecs, m)) {
        return true;
      }
    } catch (NoSuchMethodException ignored) {
    }

    return false;
  }

  @Override
  public void configure(ResourceInfo ri, FeatureContext context) {
    List<Annotation> authzSpecs = new ArrayList<>();
    Method rMethod = ri.getResourceMethod();
    for (Class<? extends Annotation> annotationClass : shiroAnnotations) {
      findAnnotation(annotationClass, authzSpecs, rMethod);
    }

    context.register(
      new ShiroThreadStateFilter(authorizationService), Priorities.AUTHORIZATION - 100
    );

    if (!authzSpecs.isEmpty()) {
      log.trace("Registering ShiroFilter.");
      context.register(
        new ShiroAuthorizationFilter(
          authorizationService,
          authzSpecs,
          ri.getResourceClass().getSimpleName(),
          ri.getResourceMethod().getName()),
        Priorities.AUTHORIZATION
      );
    }
  }
}
