package com.dropchop.recyclone.model.api.filtering;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.utils.Strings;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
@Slf4j
public class FilterSegment extends PathSegment implements Predicate<PathSegment> {

  public static FilterSegment any(Integer maxLevel) {
    return new FilterSegment(new String[]{ANY}, maxLevel);
  }

  public static FilterSegment parse(String pattern, Integer maxLevel) {
    if (pattern == null || pattern.isBlank()) {
      return null;
    }
    if (pattern.startsWith(PATH_DELIM)) {
      pattern = pattern.substring(1);
    }
    String[] pathStr = pattern.split("\\" + PATH_DELIM, 255);
    return new FilterSegment(pathStr, maxLevel);
  }

  public static boolean isCollection(Object obj) {
    return obj instanceof Collection<?> || (obj!=null && obj.getClass().isArray());
  }


  public static Class<?> getPropertyClass(PathSegment segment) {
    String propName = segment.name;
    if (propName == null) {
      return null;
    }
    try {
      Method m = segment.referer.getClass().getMethod("get" +
        propName.substring(0, 1).toUpperCase() + propName.substring(1));
      return m.getReturnType();
    } catch (NoSuchMethodException e) {
      log.warn("Unable to find property [{}] getter!", propName, e);
      return null;
    }
  }

  public static boolean isPropertyCollection(PathSegment segment) {
    String propName = segment.name;
    if (propName == null) {
      return false;
    }
    try {
      Method m = segment.referer.getClass().getMethod("get" +
        propName.substring(0, 1).toUpperCase() + propName.substring(1));
      Class<?> clazz = m.getReturnType();
      return Collection.class.isAssignableFrom(clazz);
    } catch (NoSuchMethodException e) {
      log.warn("Unable to find property [{}] getter!", propName, e);
      return false;
    }
  }

  public static boolean willPropertyNest(PathSegment segment) {
    if (segment.referer == null) {
      return false;
    }

    if (isCollection(segment.referer)) {
      return true;
    }

    if (segment.parent instanceof CollectionPathSegment && segment.referer instanceof Model) {
      return true;
    }

    String propName = segment.name;
    if (propName == null) {
      return false;
    }
    try {
      Method m = segment.referer.getClass().getMethod("get" +
        propName.substring(0, 1).toUpperCase() + propName.substring(1));
      Class<?> clazz = m.getReturnType();
      return Model.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz);
    } catch (NoSuchMethodException e) {
      log.warn("Unable to find property [{}] getter!", propName, e);
      return false;
    }
  }

  final int maxLevel;

  protected FilterSegment(String[] path, Integer maxLevel) {
    super(path);
    if (maxLevel != null && maxLevel >= 0) {
      this.maxLevel = maxLevel;
    } else {
      this.maxLevel = Integer.MAX_VALUE;
    }
  }

  public boolean isAny() {
    return this.path.length == 1 && this.path[0].equals(ANY);
  }

  public boolean startsWithAny() {
    return this.path.length > 0 && this.path[0].equals(ANY);
  }

  public boolean endsWithAny() {
    return this.path.length > 0 && this.path[this.path.length - 1].equals(ANY);
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  protected boolean testLevel(PathSegment segment) {
    /*if (segment instanceof CollectionPathSegment) {
      return segment.level < maxLevel;
    }*/
    return segment.level <= maxLevel;
  }

  protected boolean testName(PathSegment segment) {
    if (isAny()) {
      return true;
    }
    boolean startsWithAny = startsWithAny();
    if (startsWithAny) {
      this.path[0] = "**";
    }
    boolean result = Strings.matchPath(this.path, segment.indexedPath, true);
    //log.info("Match result [{}] of pat [{}] against [{}].",
      //result, String.join(PATH_DELIM, this.path), String.join(PATH_DELIM, path.indexedPath));
    if (startsWithAny) {
      this.path[0] = ANY;
    }
    return result;
  }

  @Override
  public boolean test(PathSegment segment) {
    if (!testLevel(segment)) {
      return false;
    }
    return testName(segment);
  }

  public boolean nest(PathSegment segment) {
    if (isCollection(segment.referer)) {
      if (segment.level < maxLevel) {
        return true;
      } else {
        return false;
      }
    }
    if (segment.level <= maxLevel) {
      return willPropertyNest(segment);
    }
    return false;
  }

  public boolean dive(PathSegment segment) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    if (!testLevel(segment)) {
      return false;
    }
    if (startsWithAny() || endsWithAny()) {
      return nest(segment);
    }

    return Strings.matchPath(this.path, segment.level, segment.indexedPath, segment.level, true);
  }

  @Override
  public String toString() {
    return String.join(PATH_DELIM, path) + ":" + maxLevel;
  }
}
