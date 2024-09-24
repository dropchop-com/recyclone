package com.dropchop.recyclone.model.api.marker.state;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface HasDistributedBy extends HasDistributed {
  String getDistributedBy();
  void setDistributedBy(String distributedBy);
}
