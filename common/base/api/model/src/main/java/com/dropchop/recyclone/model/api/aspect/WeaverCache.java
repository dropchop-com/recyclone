package com.dropchop.recyclone.model.api.aspect;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 13. 07. 23.
 */
public class WeaverCache {
  private static final ConcurrentHashMap<String, Field> fieldsCache = new ConcurrentHashMap<>();

  private static Field findFieldInHierarchy(Class<?> clazz, String fieldName) {
    for (Class<?> currentClass = clazz; currentClass != null; currentClass = currentClass.getSuperclass()) {
      for (Field field : currentClass.getDeclaredFields()) {
        if (field.getName().equals(fieldName)) {
          return field;
        }
      }
    }
    return null;
  }

  public static Field getField(Class<?> clazz, String fieldName) {
    String key = clazz.getName() + "#" + fieldName;
    return fieldsCache.computeIfAbsent(key, k -> findFieldInHierarchy(clazz, fieldName));
  }
}
