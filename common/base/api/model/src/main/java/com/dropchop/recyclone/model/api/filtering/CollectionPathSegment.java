package com.dropchop.recyclone.model.api.filtering;

/**
 * A path segment for collection like object in object graph traversal.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 09. 22.
 */
public class CollectionPathSegment extends PathSegment {

  int currentIndex = 0;

  public CollectionPathSegment(PathSegment parent, String name, Object referer) {
    super(parent, name, referer);
  }

  public void incCurrentIndex() {
    this.currentIndex++;
  }
}
