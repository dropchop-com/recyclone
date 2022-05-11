package com.dropchop.recyclone.model.api;

import com.dropchop.recyclone.model.api.marker.HasUuid;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@SuppressWarnings("unused")
public interface Person extends Model, HasUuid {
  String getFirstName();
  void setFirstName(String firstName);

  String getLastName();
  void setLastName(String lastName);
}
