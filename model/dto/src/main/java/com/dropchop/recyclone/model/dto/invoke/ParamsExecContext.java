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
public class ParamsExecContext<P extends Params, L extends Listener>
  implements com.dropchop.recyclone.model.api.invoke.ParamsExecContext<P, L> {

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
  P params;

  public String id() {
    return id;
  }

  public ParamsExecContext<P, L> id(String id) {
    this.setId(id);
    return this;
  }

  public Long startTime() {
    return startTime;
  }

  public ParamsExecContext<P, L> startTime(Long startTime) {
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

  public ParamsExecContext<P, L> listeners(List<L> listeners) {
    this.setListeners(listeners);
    return this;
  }

  public ParamsExecContext<P, L> listener(L listener) {
    if (listener == null) {
      return this;
    }
    this.getListeners().add(listener);
    return this;
  }

  public P params() {
    return params;
  }

  public ParamsExecContext<P, L> params(P params) {
    this.setParams(params);
    return this;
  }
}
