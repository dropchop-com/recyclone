package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.ParamsExecContext;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3. 05. 24.
 */
@Unremovable
@DefaultBean
@Priority(200)
@RequestScoped
@SuppressWarnings("unused")
public class ParamsExecContextContainer implements com.dropchop.recyclone.base.api.model.invoke.ParamsExecContextContainer {

  private ParamsExecContext<?> execContext;

  @Override
  public <D extends Dto> ParamsExecContext<?> get() {
    return execContext;
  }

  void set(ParamsExecContext<?> execContext) {
    this.execContext = execContext;
  }
}
