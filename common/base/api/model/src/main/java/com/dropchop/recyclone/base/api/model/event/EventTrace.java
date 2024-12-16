package com.dropchop.recyclone.base.api.model.event;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public interface EventTrace extends Model, HasUuid {

  String getGroup();
  void setGroup(String group);

  String getContext();
  void setContext(String context);

}
