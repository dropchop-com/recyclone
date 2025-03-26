package com.dropchop.recyclone.base.es.repo.config;

import com.dropchop.recyclone.base.api.model.utils.Strings;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/25/25.
 */
@SuppressWarnings("unused")
public interface ClassIndexConfig {

  Class<?> getRootClass();

  static String computeClassIndexName(Class<?> rootClass, String prefix) {
    String simpleName = rootClass.getSimpleName();
    if (simpleName.startsWith("Es")) {
      simpleName = simpleName.substring(2);
    }
    return prefix != null ? prefix + "_" + Strings.toSnakeCase(simpleName) : Strings.toSnakeCase(simpleName);
  }

  static String computeClassIndexName(Class<?> rootClass) {
    return computeClassIndexName(rootClass, null);
  }

  default String getDefaultIndexName() {
    String prefix = null;
    if (this instanceof HasPrefix hasPrefix) {
      prefix = hasPrefix.getPrefix();
    }
    return computeClassIndexName(getRootClass(), prefix);
  }
}
