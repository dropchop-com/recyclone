package com.dropchop.recyclone.quarkus.runtime.common;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 04. 24.
 */
public interface Factory<X> extends Provider<X> {

  default <P extends X> P create(Class<P> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  default <P extends X> P create(String paramsClassName) {
    Class<P> clazz = this.getClassFromName(paramsClassName);
    Class<X> base = getBase();
    if (!base.isAssignableFrom(clazz)) {
      throw new RuntimeException(
          "Class [" + clazz + "] does not implement [" + base + "]!"
      );
    }
    return this.create(clazz);
  }

}
