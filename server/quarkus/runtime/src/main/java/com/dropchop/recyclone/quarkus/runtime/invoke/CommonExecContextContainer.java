package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.CommonExecContext;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 04. 24.
 */
@Unremovable
@DefaultBean
@Priority(100)
@RequestScoped
@SuppressWarnings("unused")
public class CommonExecContextContainer implements com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer {

  private CommonExecContext<?, ?> execContext;

  @SuppressWarnings("unchecked")
  public <D extends Dto> CommonExecContext<D, ?> get() {
    return (CommonExecContext<D, ?>) execContext;
  }

  <D extends Dto> void set(CommonExecContext<D, ?> execContext) {
    this.execContext = execContext;
  }
}
