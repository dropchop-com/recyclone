package com.dropchop.recyclone.model.dto.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext.Listener;
import com.dropchop.recyclone.model.api.invoke.Params;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 22.
 */
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class ParamsExecContext<L extends Listener>
  implements com.dropchop.recyclone.model.api.invoke.ParamsExecContext<L> {

  @NonNull
  @Getter
  @Setter
  @EqualsAndHashCode.Include
  private String id = UUID.randomUUID().toString();

  @NonNull
  @Getter
  @Setter
  private Long startTime = System.currentTimeMillis();

  private List<L> listeners = new ArrayList<>();

  @NonNull
  @Getter
  @Setter
  Params params;

  public String id() {
    return id;
  }

  public ParamsExecContext<L> id(String id) {
    this.setId(id);
    return this;
  }

  public Long startTime() {
    return startTime;
  }

  public ParamsExecContext<L> startTime(Long startTime) {
    this.setStartTime(startTime);
    return this;
  }

  @Override
  public List<L> getListeners() {
    return listeners;
  }

  public void setListeners(List<L> listeners) {
    this.listeners = listeners;
  }

  public List<L> listeners() {
    return listeners;
  }

  public ParamsExecContext<L> listeners(List<L> listeners) {
    this.setListeners(listeners);
    return this;
  }

  public ParamsExecContext<L> listener(L listener) {
    if (listener == null) {
      return this;
    }
    this.getListeners().add(listener);
    return this;
  }

  public <P extends Params> P params() {
    //noinspection unchecked
    return (P)params;
  }

  public <P extends Params> ParamsExecContext<L> params(P params) {
    this.setParams(params);
    return this;
  }
}
