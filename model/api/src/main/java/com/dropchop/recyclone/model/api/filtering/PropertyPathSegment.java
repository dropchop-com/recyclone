package com.dropchop.recyclone.model.api.filtering;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 09. 22.
 */
public class PropertyPathSegment extends PathSegment {

  public PropertyPathSegment(PathSegment parent, String name, Object referer) {
    super(parent, name, referer);
  }
}
