package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.rest.Result;
import com.dropchop.recyclone.model.dto.invoke.DefaultExecContext;
import com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainerProvider;
import lombok.extern.slf4j.Slf4j;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static jakarta.ws.rs.Priorities.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
@Provider
public class CommonDynamicFeatures implements DynamicFeature {

  @Inject
  ExecContextContainerProvider execContextProviderProducer;

  private static void checkAdd(final Set<Class<? extends Params>> paramsClasses,
                               final Set<Class<? extends Dto>> dtoClasses,
                               final Set<Class<? extends ExecContext<?>>> execCtxClasses,
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
      @SuppressWarnings("rawtypes")
      Class<? extends ExecContext> execCtxClass = tag.execContextClass();
      if (execCtxClass != null) {
        //noinspection CastCanBeRemovedNarrowingVariableType,unchecked
        execCtxClasses.add((Class<? extends ExecContext<?>>) execCtxClass);
      }
    }
  }

  public static void findDynamicExecContextAnnotation(final Set<Class<? extends Params>> paramsClasses,
                                                      final Set<Class<? extends Dto>> dtoClasses,
                                                      final Set<Class<? extends ExecContext<?>>> execCtxClasses,
                                                      final Method myMethod) {
    DynamicExecContext[] tagsArray = myMethod.getAnnotationsByType(DynamicExecContext.class);
    checkAdd(paramsClasses, dtoClasses, execCtxClasses, tagsArray);
    Class<?> declaringClass = myMethod.getDeclaringClass();
    if (declaringClass.equals(Object.class)) {
      return;
    }
    tagsArray = declaringClass.getAnnotationsByType(DynamicExecContext.class);
    checkAdd(paramsClasses, dtoClasses, execCtxClasses, tagsArray);
    for (Class<?> iface : declaringClass.getInterfaces()) {
      try {
        Method m = iface.getMethod(myMethod.getName(), myMethod.getParameterTypes());
        findDynamicExecContextAnnotation(paramsClasses, dtoClasses, execCtxClasses, m);
      } catch (NoSuchMethodException ignored) {
      }
    }
    declaringClass = declaringClass.getSuperclass();
    if (declaringClass == null) {
      return;
    }
    try {
      Method m = declaringClass.getMethod(myMethod.getName(), myMethod.getParameterTypes());
      findDynamicExecContextAnnotation(paramsClasses, dtoClasses, execCtxClasses, m);
    } catch (NoSuchMethodException ignored) {
    }
  }

  @Override
  public void configure(ResourceInfo ri, FeatureContext context) {

    Class<?> riClass = ri.getResourceClass();
    Method method = ri.getResourceMethod();

    Class<?> ret = method.getReturnType();
    if (Result.class.isAssignableFrom(ret)) {
      context.register( // initialize and pass new params to JAX-RS context property
        new ExecContextWriteInterceptor(),
        USER
      );
    }

    Set<Class<? extends Params>> paramsClasses = new HashSet<>();
    Set<Class<? extends Dto>> dtoClasses = new HashSet<>();
    Set<Class<? extends ExecContext<?>>> execCtxClasses = new HashSet<>();

    findDynamicExecContextAnnotation(paramsClasses, dtoClasses, execCtxClasses, method);

    boolean registered = false;
    for (Class<? extends ExecContext<?>> execCtxClass : execCtxClasses) {
      if (ExecContext.class.isAssignableFrom(execCtxClass)
        && !ExecContext.class.equals(execCtxClass)
        && !DefaultExecContext.class.equals(execCtxClass)) {
        log.info("Registering [{}] with [{}] for [{}.{}].",
          ExecContextInitInterceptor.class.getSimpleName(), execCtxClass,
          riClass.getSimpleName(), method.getName());
        registered = true;
        context.register( // initialize and pass new params to JAX-RS context property
          new ExecContextInitInterceptor(execCtxClass, execContextProviderProducer),
          AUTHENTICATION
        );
      }
    }
    if (!registered) {
      log.info("Registering default [{}] with [{}] for [{}.{}].",
        ExecContextInitInterceptor.class.getSimpleName(), DefaultExecContext.class.getSimpleName(),
        riClass.getSimpleName(), method.getName());
      context.register(
        new ExecContextInitInterceptor(DefaultExecContext.class, execContextProviderProducer),
        AUTHENTICATION
      );
    }

    Class<?>[] paramTypes = method.getParameterTypes();
    for (Class<?> paramType : paramTypes) {
      if (Params.class.isAssignableFrom(paramType)) {
        log.info("Registering [{}] for [{}.{}].", ParamsInterceptor.class.getSimpleName(),
          riClass.getSimpleName(), method.getName());
        //noinspection unchecked
        context.register( // pass incoming params to JAX-RS context property
          new ParamsInterceptor((Class<? extends CommonParams<?, ?, ?, ?>>) paramType),
          HEADER_DECORATOR
        );
        return;
      }
    }

    for (Class<? extends Params> parametersClass : paramsClasses) {
      if (CommonParams.class.isAssignableFrom(parametersClass) && !CommonParams.class.equals(parametersClass)) {
        log.info("Registering [{}] for [{}.{}].",
          ParamsFactoryFilter.class.getSimpleName(), riClass.getSimpleName(), method.getName());
        context.register( // initialize and pass new params to JAX-RS context property
          new ParamsFactoryFilter(parametersClass),
          HEADER_DECORATOR
        );
      }
    }

    for (Class<? extends Dto> dtoClass : dtoClasses) {
      if (Dto.class.isAssignableFrom(dtoClass) && !Dto.class.equals(dtoClass)) {
        for (Class<?> paramType : paramTypes) {
          if (Collection.class.isAssignableFrom(paramType)) {
            log.info("Registering [{}] for [{}.{}].",
              DtoDataInterceptor.class.getSimpleName(), riClass.getSimpleName(), method.getName());
            context.register( // pass incoming dto data to JAX-RS context property
              new DtoDataInterceptor(dtoClass),
              HEADER_DECORATOR
            );
          }
        }
      }
    }
  }
}
