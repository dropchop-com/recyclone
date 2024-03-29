package com.dropchop.recyclone.model.api.marker;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface HasType {
  default String getType() {
    return this.getClass().getSimpleName();
  }

  void setType(String type);
}
