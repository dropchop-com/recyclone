package com.dropchop.recyclone.model.api.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasDeactivatedBy extends HasDeactivated {
  String getDeactivatedBy();
  void setDeactivatedBy(String deactivatedBy);
}
