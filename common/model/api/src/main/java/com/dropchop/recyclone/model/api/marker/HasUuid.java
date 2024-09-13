package com.dropchop.recyclone.model.api.marker;

import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
public interface HasUuid {

  UUID getUuid();
  void setUuid(UUID uuid);

  default void setUuid(String uuid) {
    this.setUuid(UUID.fromString(uuid));
  }
}
