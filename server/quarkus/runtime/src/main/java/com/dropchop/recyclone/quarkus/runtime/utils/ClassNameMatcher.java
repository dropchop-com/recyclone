package com.dropchop.recyclone.quarkus.runtime.utils;

import com.dropchop.recyclone.base.api.model.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 06. 24.
 */
public interface ClassNameMatcher {

  Logger log = LoggerFactory.getLogger(ClassNameMatcher.class);

  abstract class Base implements ClassNameMatcher {
    protected final String pattern;

    public Base(String pattern) {
      this.pattern = pattern;
    }
  }

  class Glob extends Base {
    private final String[] glob;

    public Glob(String pattern) {
      super(pattern);
      glob = pattern.split("\\.");
    }

    @Override
    public boolean matches(String className) {
      String[] classParts = className.split("\\.");
      return Strings.matchPath(glob, classParts, true);
    }
  }

  class RegEx extends Base {
    private final Pattern regEx;

    public RegEx(String pattern) {
      super(pattern);
      regEx = Pattern.compile(pattern);
    }

    @Override
    public boolean matches(String className) {
      return regEx.matcher(className).matches();
    }
  }

  class Extendor extends Base {

    private final Class<?> sameOrParent;

    public Extendor(String pattern) {
      super(pattern);
      Class<?> cls;
      try {
        cls = Thread.currentThread().getContextClassLoader().loadClass(pattern);
      } catch (ClassNotFoundException e) {
        log.warn("Unable to load class for parent matching {}", pattern);
        cls = null;
      }
      this.sameOrParent = cls;
    }

    @Override
    public boolean matches(String className) {
      if (className == null) {
        return false;
      }
      try {
        Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
        return sameOrParent.isAssignableFrom(cls);
      } catch (ClassNotFoundException e) {
        log.warn("Unable to load target class for parent matching {}", className);
        return false;
      }
    }
  }

  class Full extends Base {
    public Full(String pattern) {
      super(pattern);
    }

    @Override
    public boolean matches(String className) {
      if (className == null) {
        return false;
      }
      return pattern.equals(className);
    }
  }

  class Simple extends Base {
    public Simple(String pattern) {
      super(pattern);
    }

    @Override
    public boolean matches(String className) {
      if (className == null) {
        return false;
      }
      int idx = className.lastIndexOf('.');
      if (idx == -1 || idx >= className.length() - 1) {
        return pattern.equals(className);
      }
      return pattern.equals(className.substring(idx + 1));
    }
  }

  static ClassNameMatcher get(String pattern) {
    if (pattern == null || pattern.isBlank()) {
      return null;
    }
    if (pattern.startsWith("^") || pattern.endsWith("$")) {
      return new RegEx(pattern);
    }
    if (pattern.contains("*") || pattern.contains("?")) {
      return new Glob(pattern);
    }
    if (pattern.startsWith("->")) {
      return new Extendor(pattern);
    }
    if (pattern.contains(".")) {
      return new Full(pattern);
    }
    return new Simple(pattern);
  }

  boolean matches(String className);
}
