package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.utils.Uuid;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 22.
 */
@NoArgsConstructor(force = true)
@SuppressWarnings({"unused", "LombokSetterMayBeUsed"})
public class ParamsExecContext<ECL extends Listener>
  implements com.dropchop.recyclone.model.api.invoke.ParamsExecContext<ECL> {

  @NonNull
  @Getter
  @Setter
  @EqualsAndHashCode.Include
  private String id = Uuid.getTimeBased().toString();

  @NonNull
  @Getter
  @Setter
  private Long startTime = System.currentTimeMillis();

  private List<ECL> listeners = new ArrayList<>();

  @NonNull
  @Getter
  @Setter
  Params params;

  public String id() {
    return id;
  }

  public ParamsExecContext<ECL> id(String id) {
    this.setId(id);
    return this;
  }

  public Long startTime() {
    return startTime;
  }

  public ParamsExecContext<ECL> startTime(Long startTime) {
    this.setStartTime(startTime);
    return this;
  }

  @Override
  public List<ECL> getListeners() {
    return listeners;
  }

  public void setListeners(List<ECL> listeners) {
    this.listeners = listeners;
  }

  public ParamsExecContext<ECL> listener(ECL listener) {
    com.dropchop.recyclone.model.api.invoke.ParamsExecContext.super.listener(listener);
    return this;
  }

  public List<ECL> listeners() {
    return listeners;
  }

  public <P extends Params> P params() {
    //noinspection unchecked
    return (P)params;
  }

  public <P extends Params> ParamsExecContext<ECL> params(P params) {
    this.setParams(params);
    return this;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + getId() + ", p:" + params;
  }
}
