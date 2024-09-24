package com.dropchop.recyclone.model.api.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasModifiedBy extends HasModified {
  String getModifiedBy();
  void setModifiedBy(String modifiedBy);
}
