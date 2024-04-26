package com.dropchop.recyclone.quarkus.runtime.common;

import jakarta.enterprise.inject.Instance;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 04. 24.
 */
public interface Selector<X> extends Provider<X> {

  default <P extends X> P select(Class<P> clazz) {
    Class<X> base = getBase();
    if (!base.isAssignableFrom(clazz)) {
      throw new RuntimeException(
          "Class [" + clazz + "] does not implement [" + base + "]!"
      );
    }

    Instance<P> candidates =  getInstances().select(clazz);
    if (candidates.stream().findAny().isEmpty()) {
      throw new RuntimeException("Missing class [" + clazz + "] implementation!");
    }

    return candidates.iterator().next();
  }

  default <P extends X> P select(String className) {
    Class<P> clazz = this.getClassFromName(className);
    return this.select(clazz);
  }
}
