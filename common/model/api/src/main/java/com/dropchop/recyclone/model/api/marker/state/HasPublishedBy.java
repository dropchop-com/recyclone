package com.dropchop.recyclone.model.api.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasPublishedBy extends HasPublished {
  String getPublishedBy();
  void setPublishedBy(String publishedBy);
}
