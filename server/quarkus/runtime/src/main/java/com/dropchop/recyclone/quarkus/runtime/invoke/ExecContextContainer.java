package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.base.api.model.invoke.ExecContext;
import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.RequestScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 26. 04. 24.
 */
@Unremovable
@DefaultBean
@Priority(300)
@RequestScoped
@SuppressWarnings("unused")
public class ExecContextContainer implements com.dropchop.recyclone.base.api.model.invoke.ExecContextContainer {

  private ExecContext<?> execContext;

  public ExecContext<?> get() {
    return execContext;
  }

  void set(ExecContext<?> execContext) {
    this.execContext = execContext;
  }
}
