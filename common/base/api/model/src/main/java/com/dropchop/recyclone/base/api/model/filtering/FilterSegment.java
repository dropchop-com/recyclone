package com.dropchop.recyclone.base.api.model.filtering;

import com.dropchop.recyclone.base.api.model.utils.Objects;
import com.dropchop.recyclone.base.api.model.utils.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 09. 22.
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
    if (Objects.isCollectionLike(segment.referer)) {
      return segment.level < maxLevel;
    }
    if (segment.propertyClass != null) {
      if (segment.collectionLike) {
        return segment.level < maxLevel;
      }
      if (segment.level <= maxLevel) {
        return segment.modelLike;
      }
    }
    return false;
  }

  boolean dive(PathSegment segment, Boolean precomputedNest) {
    if (segment.parent == null) {// we always accept root
      return true;
    }
    if (!testLevel(segment)) {
      return false;
    }
    if (startsWithAny() || endsWithAny()) {
      return precomputedNest != null ? precomputedNest : nest(segment);
    }

    return Strings.matchPath(this.path, segment.level, segment.indexedPath, segment.level, true);
  }

  @SuppressWarnings("SameParameterValue")
  boolean dive(PathSegment segment, boolean precomputedNest) {
    return dive(segment, (Boolean) precomputedNest);
  }

  public boolean dive(PathSegment segment) {
    return dive(segment, null);
  }

  @Override
  public String toString() {
    return String.join(PATH_DELIM, path) + ":" + maxLevel;
  }
}
