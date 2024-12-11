package com.dropchop.recyclone.model.api.event;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
public interface Event<
    D extends EventDetail,
    I extends EventItem<D>,
    T extends EventTrace
    > extends Model, HasUuid, HasCreated, HasAttributes {


  String getApplication();
  void setApplication(String application);

  String getType();
  void setType(String type);

  String getAction();
  void setAction(String action);

  String getData();
  void setData(String data);

  Double getValue();
  void setValue(Double value);

  String getUnit();
  void setUnit(String unit);

  I getSource();
  void setSource(I source);

  I getTarget();
  void setTarget(I target);

  I getCause();
  void setCause(I cause);

  T getTrace();
  void setTrace(T trace);
}
