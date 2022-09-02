package com.dropchop.recyclone.model.dto.filtering;

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

  public static FilterSegment root(Integer maxLevel) {
    return new FilterSegment(null, ROOT_OBJECT, maxLevel);
  }

  public static FilterSegment any(Integer maxLevel) {
    return new FilterSegment(root(maxLevel), ANY, maxLevel);
  }

  public static FilterSegment parse(String pattern, Integer maxLevel) {
    if (pattern == null || pattern.isBlank()) {
      return null;
    }
    if (pattern.startsWith(PATH_DELIM)) {
      pattern = pattern.substring(1);
    }
    String[] pathStr = pattern.split("\\" + PATH_DELIM, 255);
    FilterSegment segment = FilterSegment.root(maxLevel);
    for (String segStr : pathStr) {
      segment = new FilterSegment(segment, segStr, maxLevel);
    }
    return segment;
  }

  final int maxLevel;

  public FilterSegment(PathSegment parent, String name, Integer maxLevel) {
    super(parent, name);
    if (maxLevel != null && maxLevel >= 0) {
      this.maxLevel = maxLevel;
    } else {
      this.maxLevel = Integer.MAX_VALUE;
    }
  }

  public boolean willPropertyNest(PathSegment path) {
    if (path.referer == null) {
      return false;
    }
    String propName = path.name;
    try {
      Method m = path.referer.getClass().getMethod("get" + propName.substring(0, 1).toUpperCase() + propName.substring(1));
      Class<?> clazz = m.getReturnType();
      return Model.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz);
    } catch (NoSuchMethodException e) {
      log.warn("Unable to find property [{}] getter!", propName, e);
      return false;
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

  public boolean filterLevel(PathSegment path) {
    return path.level <= maxLevel;
  }

  public boolean filterName(PathSegment path) {
    if (isAny()) {
      return true;
    }
    boolean startsWithAny = startsWithAny();
    if (startsWithAny) {
      this.path[0] = "**";
    }
    boolean result = Strings.matchPath(this.path, path.indexedPath, true);
    //log.info("Match result [{}] of pat [{}] against [{}].",
      //result, String.join(PATH_DELIM, this.path), String.join(PATH_DELIM, path.indexedPath));
    if (startsWithAny) {
      this.path[0] = ANY;
    }
    return result;
  }

  @Override
  public boolean test(PathSegment path) {
    if (path.parent == null) {// we always accept root
      return true;
    }
    if (!filterLevel(path)) {
      return false;
    }
    return filterName(path);
  }

  public boolean dive(PathSegment path) {
    if (path.parent == null) {// we always accept root
      return true;
    }
    if (startsWithAny() || endsWithAny()) {
      if (path.level < maxLevel) {
        return willPropertyNest(path);
      }
      return false;
    }

    return Strings.matchPath(this.path, path.level, path.indexedPath, path.level, true);
  }

  @Override
  public String toString() {
    return String.join(PATH_DELIM, path) + ":" + maxLevel;
  }
}
