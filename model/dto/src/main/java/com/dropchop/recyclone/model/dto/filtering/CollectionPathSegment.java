package com.dropchop.recyclone.model.dto.filtering;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
public class CollectionPathSegment extends PathSegment {

  int currentIndex = 0;

  public CollectionPathSegment(PathSegment parent, String name, Object referer) {
    super(parent, name, referer);
  }

  public CollectionPathSegment(PathSegment parent, String name) {
    super(parent, name);
  }

  public CollectionPathSegment(String name, Object referer) {
    super(name, referer);
  }

  public CollectionPathSegment(String name) {
    super(name);
  }

  public void incCurrentIndex() {
    this.currentIndex++;
  }
}
