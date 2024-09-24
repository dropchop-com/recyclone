package com.dropchop.recyclone.model.api.filtering;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.utils.Objects;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;

/**
 * PathSegment is an object representing current path in object graph traversal.
 * It contains:
 *  - the current object reference
 *  - parent segment reference
 *  - computed level
 *  - computed String[] path of segments
 *  - an index if parent is a collection like object
 *  - class of a property/segment which is usually current object's class
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 09. 22.
 */
@SuppressWarnings("UnusedReturnValue")
@Slf4j
public class PathSegment {
  public static final String ROOT_OBJECT = ".";
  public static final String PATH_DELIM = ".";
  //public static final String SIBLING_DELIM = ";";
  public static final String ANY = "*";


  public static Class<?> getPropertyClass(PathSegment segment) {
    if (segment.parent instanceof CollectionPathSegment) {
      return segment.referer.getClass();
    }
    return Objects.getPropertyClass(segment.referer, segment.name);
  }

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
  public final Class<?> propertyClass;

  public final boolean collectionLike;
  public final boolean modelLike;

  private boolean dive = true;

  public PathSegment(PathSegment parent, String name, Object referer) {
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

        if (Objects.isCollectionLike(this.referer)) {
          this.propertyClass = Collection.class;
          this.collectionLike = true;
          this.modelLike = false;
        } else {
          Class<?> refererClass = (referer != null ? referer.getClass() : null);
          this.propertyClass = this instanceof PropertyPathSegment ? getPropertyClass(this) : refererClass;
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
