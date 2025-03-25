package com.dropchop.recyclone.base.api.model.event;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public interface EventDetail<D extends EventDetail> extends Model {

  String getId();
  void setId(String id);

  String getDescriptor();
  void setDescriptor(String descriptor);

  String getName();
  void setName(String name);

  String getValue();
  void setValue(String value);

  D getParent();
  void setParent(D parent);

  D getChild();
  void setChild(D child);

}
