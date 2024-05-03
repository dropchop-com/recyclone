package com.dropchop.recyclone.quarkus.runtime.common;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.impl.ParameterizedTypeImpl;

import java.lang.reflect.Type;

public interface ParameterizedSelector<X, Y> {

  default <R extends X, P extends Y> R select(Class<R> rawClass, Class<P> parameterClass) {
    ArcContainer container = Arc.container();

    // Create an instance of ParameterizedTypeImpl for rawType<parameterType>
    Type type;
    if (parameterClass != null) {
      type = new ParameterizedTypeImpl(rawClass, parameterClass);
    } else {
      type = new ParameterizedTypeImpl(rawClass);
    }

    // Getting the Instance
    InstanceHandle<R> instanceHandle = container.instance(type);
    if (!instanceHandle.isAvailable()) {
      throw new RuntimeException("Missing class [" + rawClass + "<" + parameterClass + ">] implementation!");
    }

    // If instance exists, return; else return null
    return instanceHandle.get();
  }
}
