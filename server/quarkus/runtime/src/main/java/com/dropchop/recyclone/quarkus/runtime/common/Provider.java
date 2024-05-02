package com.dropchop.recyclone.quarkus.runtime.common;

/**
 * Base class for Factory or Selector.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 04. 24.
 */
public interface Provider<X> {

  Class<X> getBase();

  default <P extends X> Class<P> getClassFromName(String clazzName) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    Class<P> clazz;
    try {
      //noinspection unchecked
      clazz = (Class<P>) cl.loadClass(clazzName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return clazz;
  }
}
