package com.dropchop.recyclone.model.dto.filtering;

import java.util.Arrays;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
public class PathSegment {
  public static final String ROOT_OBJECT = ".";
  public static final String PATH_DELIM = ".";
  public static final String SIBLING_DELIM = ";";
  public static final String ANY = "*";

  public static PathSegment root(Object referer) {
    return new PathSegment(null, ROOT_OBJECT, referer);
  }

  public final String name;
  public final Object referer;
  public final int index;

  public final PathSegment parent;
  public final int level;
  public final String[] path;
  public final String[] indexedPath;

  public PathSegment(PathSegment parent, String name, Object referer) {
    this.name = name;
    this.referer = referer;
    this.parent = parent;
    if (parent != null) {
      if (parent instanceof CollectionPathSegment collSegment) {
        this.index = collSegment.currentIndex;
        this.path = Arrays.copyOf(parent.path, parent.path.length + 1);
        this.path[path.length - 1] = name;
        this.indexedPath = Arrays.copyOf(parent.indexedPath, parent.indexedPath.length + 1);
        this.indexedPath[this.indexedPath.length - 2] += "[" + this.index + "]";
        this.indexedPath[this.indexedPath.length - 1] = name;
      } else {
        this.index = -1;
        this.path = Arrays.copyOf(parent.path, parent.path.length + 1);
        this.path[path.length - 1] = name;
        this.indexedPath = path;
      }
    } else {
      this.index = -1;
      this.path = new String[]{};
      this.indexedPath = path;
    }

    this.level = parent != null ? parent.level + 1 : 0;
  }

  public PathSegment(PathSegment parent, String name) {
    this(parent, name, null);
  }

  public PathSegment(String name, Object referer) {
    this(null, name, referer);
  }

  public PathSegment(String name) {
    this(null, name, null);
  }

  boolean isCollectionElement() {
    return this.index > -1;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ",l:" + level
      + (referer != null ? ",r:" + referer.getClass().getSimpleName() : "null")
      + ",p:" + String.join(PATH_DELIM, indexedPath);
  }
}
