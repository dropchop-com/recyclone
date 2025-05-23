package com.dropchop.recyclone.base.api.model.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasRemovedBy extends HasRemoved {
  String getRemovedBy();
  void setRemovedBy(String removedBy);
}
