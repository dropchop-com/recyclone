package com.dropchop.recyclone.base.api.model.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 09. 22.
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

  public static boolean isSubtypeOrImplements(Class<?> subclass, Class<?> superclassOrInterface) {
    // Check direct equality
    if (subclass.equals(superclassOrInterface)) {
      return true;
    }

    // Check superclass hierarchy
    Class<?> superClass = subclass.getSuperclass();
    if (superClass != null && isSubtypeOrImplements(superClass, superclassOrInterface)) {
      return true;
    }

    // Check implemented interfaces
    for (Class<?> interfaceClass : subclass.getInterfaces()) {
      if (isSubtypeOrImplements(interfaceClass, superclassOrInterface)) {
        return true;
      }
    }

    return false; // No relationship found
  }


  public static boolean isWithinBounds(Class<?> genericClass, Class<?> otherClass) {
    // Get type parameters of the generic class
    TypeVariable<?>[] typeParameters = genericClass.getTypeParameters();

    for (TypeVariable<?> typeParameter : typeParameters) {
      // Inspect bounds of each type parameter
      for (Type bound : typeParameter.getBounds()) {
        // Check if the otherClass matches or is assignable to this bound
        if (bound instanceof Class<?> boundClass) {
          if (isSubtypeOrImplements(otherClass, boundClass)) {
            return true; // Match found
          }
        }
      }
    }
    return false; // No match found
  }
}
