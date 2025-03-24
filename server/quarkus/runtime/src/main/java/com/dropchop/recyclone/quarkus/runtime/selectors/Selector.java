package com.dropchop.recyclone.quarkus.runtime.selectors;

import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.impl.ParameterizedTypeImpl;

import java.lang.reflect.Type;

/**
 * Base interface for CDI bean selection.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 04. 24.
 */
public interface Selector<X> {

  default <P extends X> P select(Class<P> rawClass) {
    ArcContainer container = Arc.container();

    // Create an instance of ParameterizedTypeImpl for rawType<parameterType>
    Type type = new ParameterizedTypeImpl(rawClass);

    // Getting the Instance
    try (InstanceHandle<P> instanceHandle = container.instance(type)) {
      if (!instanceHandle.isAvailable()) {
        return null;
      }

      // If instance exists, return; else return null
      return instanceHandle.get();
    }
  }

  default <P extends X> P selectOrThrow(Class<P> rawClass) {
    P instance = select(rawClass);
    if (instance == null) {
      throw new RuntimeException("Missing class [" + rawClass + "] implementation!");
    }
    return instance;
  }
}
