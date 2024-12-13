package com.dropchop.recyclone.base.api.model.filtering;

/**
 * This a path segment constructed from Object and its property.
 * (Forward looking)
 * Here propertyClass field is a class returned by property getter method.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 09. 22.
 */
public class PropertyPathSegment extends PathSegment {

  public PropertyPathSegment(PathSegment parent, String name, Object referer) {
    super(parent, name, referer);
  }
}
