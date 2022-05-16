package com.dropchop.recyclone.rest.jaxrs.server;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Priorities;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
@Provider
public class CommonDynamicFeatures implements DynamicFeature {

  private static void checkAdd(final Set<Class<? extends Params>> paramsClasses,
                               final Set<Class<? extends Dto>> dtoClasses,
                               DynamicExecContext[] tagsArray) {
    for (DynamicExecContext tag : tagsArray) {
      Class<? extends Params> paramsClass = tag.value();
      if (paramsClass != null) {
        paramsClasses.add(paramsClass);
      }
      Class<? extends Dto> dtoClass = tag.dataClass();
      if (dtoClass != null) {
        dtoClasses.add(dtoClass);
      }
    }
  }

  public static void findDynamicExecContextAnnotation(final Set<Class<? extends Params>> paramsClasses,
                                                      final Set<Class<? extends Dto>> dtoClasses,
                                                      final Method myMethod) {
    DynamicExecContext[] tagsArray = myMethod.getAnnotationsByType(DynamicExecContext.class);
    checkAdd(paramsClasses, dtoClasses, tagsArray);
    Class<?> declaringClass = myMethod.getDeclaringClass();
    if (declaringClass.equals(Object.class)) {
      return;
    }
    tagsArray = declaringClass.getAnnotationsByType(DynamicExecContext.class);
    checkAdd(paramsClasses, dtoClasses, tagsArray);
    for (Class<?> iface : declaringClass.getInterfaces()) {
      try {
        Method m = iface.getMethod(myMethod.getName(), myMethod.getParameterTypes());
        findDynamicExecContextAnnotation(paramsClasses, dtoClasses, m);
      } catch (NoSuchMethodException ignored) {
      }
    }
    declaringClass = declaringClass.getSuperclass();
    if (declaringClass == null) {
      return;
    }
    try {
      Method m = declaringClass.getMethod(myMethod.getName(), myMethod.getParameterTypes());
      findDynamicExecContextAnnotation(paramsClasses, dtoClasses, m);
    } catch (NoSuchMethodException ignored) {
    }
  }

  @Override
  public void configure(ResourceInfo ri, FeatureContext context) {

    context.register(
      new ExecContextCreateFilter(), Priorities.AUTHENTICATION
    );

    context.register(
      new ExecContextWriteInterceptor(), Priorities.USER
    );

    context.register(
      new ExecContextDestroyInterceptor(), Priorities.USER + 1000
    );

    Class<?> riClass = ri.getResourceClass();
    Method method = ri.getResourceMethod();

    Class<?>[] paramTypes = method.getParameterTypes();
    for (Class<?> paramType : paramTypes) {
      if (Params.class.isAssignableFrom(paramType)) {
        log.info("Registering [{}] for [{}.{}].", ParamsInterceptor.class.getSimpleName(), riClass.getSimpleName(), method.getName());
        //noinspection unchecked
        context.register(//register incoming params to thread local upon request
          new ParamsInterceptor((Class<? extends Params>) paramType),
          Priorities.HEADER_DECORATOR
        );
        return;
      }
    }

    Set<Class<? extends Params>> paramsClasses = new HashSet<>();
    Set<Class<? extends Dto>> dtoClasses = new HashSet<>();
    findDynamicExecContextAnnotation(paramsClasses, dtoClasses, method);
    for (Class<? extends Params> parametersClass : paramsClasses) {
      if (Params.class.isAssignableFrom(parametersClass) && !Params.class.equals(parametersClass)) {
        log.info("Registering [{}] for [{}.{}].",
          ParamsFilterFactory.class.getSimpleName(), riClass.getSimpleName(), method.getName());
        context.register(//register new params to thread local execution context and decorate params upon request
          new ParamsFilterFactory(parametersClass),
          Priorities.HEADER_DECORATOR
        );
      }
    }
    for (Class<? extends Dto> dtoClass : dtoClasses) {
      if (Dto.class.isAssignableFrom(dtoClass) && !Dto.class.equals(dtoClass)) {
        for (Class<?> paramType : paramTypes) {
          if (Collection.class.isAssignableFrom(paramType)) {
            log.info("Registering [{}] for [{}.{}].",
              DtoDataInterceptor.class.getSimpleName(), riClass.getSimpleName(), method.getName());
            context.register(//register new params to thread local execution context and decorate params upon request
              new DtoDataInterceptor(dtoClass),
              Priorities.HEADER_DECORATOR
            );
          }
        }
      }
    }
  }
}
