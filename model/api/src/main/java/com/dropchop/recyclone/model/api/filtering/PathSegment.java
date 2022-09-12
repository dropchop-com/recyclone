package com.dropchop.recyclone.model.api.filtering;

import com.dropchop.recyclone.model.api.base.Model;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
@SuppressWarnings("UnusedReturnValue")
@Slf4j
public class PathSegment {
  public static final String ROOT_OBJECT = ".";
  public static final String PATH_DELIM = ".";
  public static final String SIBLING_DELIM = ";";
  public static final String ANY = "*";

  public static boolean isCollection(Object obj) {
    return obj instanceof Collection<?> || (obj!=null && obj.getClass().isArray());
  }

  public static Class<?> getPropertyClass(PathSegment segment) {
    if (segment.referer == null) {
      return null;
    }
    if (segment.parent instanceof CollectionPathSegment) {
      return segment.referer.getClass();
    }
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

  public static PathSegment root(Object referer) {
    return new PathSegment(null, ROOT_OBJECT, referer, true);
  }

  public final String name;
  public final Object referer;
  public final int index;

  public final PathSegment parent;
  public final int level;
  public final String[] path;
  public final String[] indexedPath;
  public final Class<?> propertyClass;

  public final boolean collectionLike;
  public final boolean modelLike;

  private boolean dive = true;

  public static PathSegment fromContainer(PathSegment parent, String name, Object referer) {
    return new PathSegment(parent, name, referer, true);
  }

  protected PathSegment(PathSegment parent, String name, Object referer, boolean forwardPropertyLookUp) {
    this.referer = referer;
    this.parent = parent;
    if (parent != null) {
      if (parent instanceof CollectionPathSegment collSegment) {
        this.index = collSegment.currentIndex;
        this.path = Arrays.copyOf(parent.path, parent.path.length);
        this.indexedPath = Arrays.copyOf(parent.indexedPath, parent.indexedPath.length);
        this.indexedPath[this.indexedPath.length - 1] += "[" + this.index + "]";
        //this.level = parent.level;
        this.name = name == null ? "[" + this.index + "]" : name;
      } else {
        this.index = -1;
        this.path = Arrays.copyOf(parent.path, parent.path.length + 1);
        this.path[path.length - 1] = name;
        this.indexedPath = Arrays.copyOf(parent.indexedPath, parent.indexedPath.length + 1);
        this.indexedPath[indexedPath.length - 1] = name;
        this.name = name;
      }
      if (this instanceof CollectionPathSegment) {
        this.level = parent.level;

        this.propertyClass = Collection.class;
        this.collectionLike = true;
        this.modelLike = false;
      } else {
        if (referer == parent.referer) {
          this.level = parent.level;
        } else {
          this.level = parent.level + 1;
        }

        if (isCollection(this.referer)) {
          this.propertyClass = Collection.class;
          this.collectionLike = true;
          this.modelLike = false;
        } else {
          this.propertyClass = !forwardPropertyLookUp ? (referer != null ? referer.getClass() : null) : getPropertyClass(this);
          this.collectionLike = this.propertyClass != null && Collection.class.isAssignableFrom(this.propertyClass);
          this.modelLike = this.propertyClass != null && Model.class.isAssignableFrom(this.propertyClass);
        }
      }
    } else {
      this.name = name == null ? ROOT_OBJECT : name;
      this.index = -1;
      this.path = new String[]{};
      this.indexedPath = path;
      this.level = 1;
      this.propertyClass = null;
      this.collectionLike = false;
      this.modelLike = false;
    }
  }

  public PathSegment(PathSegment parent, String name, Object referer) {
    this(parent, name, referer, false);
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
    this.propertyClass = null;
    this.collectionLike = false;
    this.modelLike = false;
  }

  public boolean dive() {
    return dive;
  }

  public PathSegment dive(boolean dive) {
    this.dive = dive;
    return this;
  }

  public boolean nestable() {
    return collectionLike || modelLike;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ",l:" + level
      + (referer != null ? ",r:" + referer.getClass().getSimpleName() : ",r:null")
      + ",p:" + String.join(PATH_DELIM, indexedPath);
  }
}
