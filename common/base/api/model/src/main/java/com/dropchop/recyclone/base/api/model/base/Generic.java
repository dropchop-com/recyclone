package com.dropchop.recyclone.base.api.model.base;

import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 5/22/25.
 */
@SuppressWarnings("unused")
public interface Generic extends Model, HasId, Titled, HasDeactivated, HasModified {
  String getType();
  void setType(String type);

  List<String> getGroups();
  void setGroups(List<String> groups);
}
