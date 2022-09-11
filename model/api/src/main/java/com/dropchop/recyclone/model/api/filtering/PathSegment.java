package com.dropchop.recyclone.model.api.filtering;

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

  private boolean dive = true;
  private boolean test = true;

  public PathSegment(PathSegment parent, String name, Object referer) {
    this.referer = referer;
    this.parent = parent;
    if (parent != null) {
      if (parent instanceof CollectionPathSegment collSegment) {
        this.index = collSegment.currentIndex;
        this.path = Arrays.copyOf(parent.path, parent.path.length);
        this.indexedPath = Arrays.copyOf(parent.indexedPath, parent.indexedPath.length);
        this.indexedPath[this.indexedPath.length - 1] += "[" + this.index + "]";
        this.level = parent.level;
        this.name = name == null ? "[" + this.index + "]" : name;
      } else {
        this.index = -1;
        this.path = Arrays.copyOf(parent.path, parent.path.length + 1);
        this.path[path.length - 1] = name;
        this.indexedPath = Arrays.copyOf(parent.indexedPath, parent.indexedPath.length + 1);
        this.indexedPath[indexedPath.length - 1] = name;
        this.level = parent.level + 1;
        this.name = name;
      }
    } else {
      this.name = name == null ? ROOT_OBJECT : name;
      this.index = -1;
      this.path = new String[]{};
      this.indexedPath = path;
      this.level = 0;
    }
  }

  protected PathSegment(String[] path) {
    if (path == null) {
      path = new String[]{};
    }
    this.name = path.length > 0 ? path[path.length - 1] : ROOT_OBJECT;
    this.referer = null;
    this.parent = null;
    this.path = path;
    this.indexedPath = path;
    this.level = path.length;
    this.index = -1;
  }

  boolean isCollectionElement() {
    return this.index > -1;
  }

  public boolean dive() {
    return dive;
  }

  public PathSegment dive(boolean dive) {
    this.dive = dive;
    return this;
  }

  public boolean test() {
    return test;
  }

  public PathSegment test(boolean test) {
    this.test = test;
    return this;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ",l:" + level
      + (referer != null ? ",r:" + referer.getClass().getSimpleName() : ",r:null")
      + ",p:" + String.join(PATH_DELIM, indexedPath);
  }
}
