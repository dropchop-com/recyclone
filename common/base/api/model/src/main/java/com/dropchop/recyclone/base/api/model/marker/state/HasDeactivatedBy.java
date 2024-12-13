package com.dropchop.recyclone.base.api.model.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasDeactivatedBy extends HasDeactivated {
  String getDeactivatedBy();
  void setDeactivatedBy(String deactivatedBy);
}
