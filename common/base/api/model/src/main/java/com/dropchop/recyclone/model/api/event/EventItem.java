package com.dropchop.recyclone.model.api.event;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public interface EventItem<D extends EventDetail> extends Model, HasUuid, HasCreated {

  D getSubject();
  void setSubject(D subject);

  D getObject();
  void setObject(D object);

  D getService();
  void setService(D service);

  D getContext();
  void setContext(D context);

}
