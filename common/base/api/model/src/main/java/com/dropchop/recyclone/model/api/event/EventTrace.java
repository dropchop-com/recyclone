package com.dropchop.recyclone.model.api.event;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public interface EventTrace extends Model, HasUuid {

  String getGroup();
  void setGroup(String group);

  String getContext();
  void setContext(String context);

}
