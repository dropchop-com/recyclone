package com.dropchop.recyclone.quarkus.deployment.utils;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.Type;
import org.jboss.jandex.TypeVariable;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 12. 24.
 */
public class JandexTypeBoundsChecker {

  private static boolean isSubtypeOrImplements(ClassInfo subclass, ClassInfo superclassOrInterface, IndexView index) {
    // Check direct equality
    if (subclass.name().equals(superclassOrInterface.name())) {
      return true;
    }

    // Check superclass hierarchy
    Type superType = subclass.superClassType();
    if (superType != null && superType.kind() == Type.Kind.CLASS) {
      ClassInfo superClass = index.getClassByName(superType.asClassType().name());
      if (superClass != null && isSubtypeOrImplements(superClass, superclassOrInterface, index)) {
        return true;
      }
    }

    // Check implemented interfaces
    for (Type interfaceType : subclass.interfaceTypes()) {
      if (interfaceType.kind() == Type.Kind.CLASS) {
        ClassInfo interfaceClass = index.getClassByName(interfaceType.asClassType().name());
        if (interfaceClass != null && isSubtypeOrImplements(interfaceClass, superclassOrInterface, index)) {
          return true;
        }
      }
    }

    return false; // No relationship found
  }

  public static boolean isWithinBounds(ClassInfo genericClass, ClassInfo otherClass, IndexView index) {
    // Get type parameters of the generic class
    for (TypeVariable typeVariable : genericClass.typeParameters()) {
      // Inspect bounds of each type parameter
      for (Type bound : typeVariable.bounds()) {
        if (bound.kind() == Type.Kind.CLASS) {
          ClassInfo boundClass = index.getClassByName(bound.asClassType().name());
          if (boundClass != null && isSubtypeOrImplements(otherClass, boundClass, index)) {
            return true; // Match found
          }
        }
      }
    }
    return false; // No match found
  }
}
