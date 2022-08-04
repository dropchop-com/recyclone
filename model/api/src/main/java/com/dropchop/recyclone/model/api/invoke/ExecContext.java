package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasId;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 22.
 */
public interface ExecContext<ECL extends ExecContext.Listener> extends Model, HasId {
  interface Listener {
  }

  Long getStartTime();
  void setStartTime(Long startTime);

  default Long getExecTime() {
    Long start = getStartTime();
    if (start == null) {
      return 0L;
    }
    return System.currentTimeMillis() - start;
  }

  List<ECL> getListeners();
  void setListeners(List<ECL> listeners);

  default ExecContext<ECL> listener(ECL listener) {
    if (listener == null) {
      return this;
    }
    this.getListeners().add(listener);
    return this;
  }


  default <EC extends ExecContext<? extends Listener>> EC copyAs(EC context) {
    String id = getId();
    if (id != null) {
      context.setId(id);
    }
    Long startTime = getStartTime();
    if (startTime != null) {
      context.setStartTime(startTime);
    }
    return context;
  }


  default ExecContext<ECL> of(ExecContext<?> sourceContext) {
    String id = sourceContext.getId();
    if (id != null) {
      this.setId(id);
    }
    Long startTime = sourceContext.getStartTime();
    if (startTime != null) {
      this.setStartTime(startTime);
    }
    return this;
  }
}
