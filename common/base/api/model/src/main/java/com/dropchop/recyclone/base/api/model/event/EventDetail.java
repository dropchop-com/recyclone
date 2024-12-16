package com.dropchop.recyclone.base.api.model.event;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public interface EventDetail<D extends EventDetail> extends Model, HasUuid, HasCreated {

  String getName();
  void setName(String name);

  D getParent();
  void setParent(D parent);

  D getChild();
  void setChild(D child);

}
