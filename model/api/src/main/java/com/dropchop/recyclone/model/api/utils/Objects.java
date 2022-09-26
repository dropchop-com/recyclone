package com.dropchop.recyclone.model.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 09. 22.
 */
public class Objects {

  private static final Logger log = LoggerFactory.getLogger(Objects.class);

  public static boolean isCollectionLike(Object obj) {
    return obj instanceof Collection<?> || (obj!=null && obj.getClass().isArray());
  }

  public static Class<?> getPropertyClass(Object object, String propName) {
    if (object == null) {
      return null;
    }
    if (propName == null) {
      return null;
    }
    String postfix = propName.substring(0, 1).toUpperCase() + propName.substring(1);
    try {
      Method m = object.getClass().getMethod("get" + postfix);
      return m.getReturnType();
    } catch (NoSuchMethodException e0) {
      try {
        Method m = object.getClass().getMethod("is" + postfix);
        return m.getReturnType();
      } catch (NoSuchMethodException e1) {
        log.warn("Unable to find property [{}] getter!", propName, e1);
        return null;
      }
    }
  }
}
