package com.dropchop.recyclone.model.api.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasCreatedBy extends HasCreated {
  String getCreatedBy();
  void setCreatedBy(String createdBy);
}
