package com.dropchop.recyclone.base.api.model.event;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public interface EventItem<D extends EventDetail> extends Model {

  D getSubject();
  void setSubject(D subject);

  D getObject();
  void setObject(D object);

  D getService();
  void setService(D service);

  D getContext();
  void setContext(D context);
}
