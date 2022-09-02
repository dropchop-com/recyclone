package com.dropchop.recyclone.model.dto.filtering;

import java.util.Collection;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
public class MarkerFilterSegment extends FilterSegment {

  public final Collection<Class<?>> markers;

  public MarkerFilterSegment(PathSegment parent, String name, Integer maxLevel, Collection<Class<?>> markers) {
    super(parent, name, maxLevel);
    this.markers = markers;
  }

  public MarkerFilterSegment(PathSegment parent, String name, Collection<Class<?>> markers) {
    this(parent, name, Integer.MAX_VALUE, markers);
  }

  public MarkerFilterSegment(PathSegment parent, String name, Integer maxLevel, Class<?> ... markers) {
    this(parent, name, maxLevel, List.of(markers));
  }

  public MarkerFilterSegment(PathSegment parent, String name, Class<?> ... markers) {
    this(parent, name, Integer.MAX_VALUE, List.of(markers));
  }

  public boolean filter(PathSegment path) {
    /*if (filterLevel(path)) {
      return true;
    }
    if (this.referer == null) {

    }*/
    return false;
  }
}
