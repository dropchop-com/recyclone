package com.dropchop.recyclone.base.api.model.marker;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
public interface HasType {
  default String getType() {
    return this.getClass().getSimpleName();
  }

  void setType(String type);
}
