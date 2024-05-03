package com.dropchop.recyclone.quarkus.runtime.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import jakarta.enterprise.context.RequestScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 04. 24.
 */
@RequestScoped
@SuppressWarnings("unused")
public class ExecContextContainer {

  private ExecContext<?> execContext;

  public ExecContext<?> get() {
    return execContext;
  }

  public void set(ExecContext<?> execContext) {
    this.execContext = execContext;
  }
}
