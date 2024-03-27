package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.invoke.CommonExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.model.dto.invoke.DefaultExecContext;
import com.dropchop.recyclone.service.api.ExecContextType;
import jakarta.enterprise.context.RequestScoped;
import lombok.extern.slf4j.Slf4j;

/**
 * Container and CDI provider for Request scoped {@link com.dropchop.recyclone.model.api.invoke.ExecContext}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 24. 05. 22.
 */
@Slf4j
@RequestScoped
@ExecContextType(Constants.Implementation.RCYN_DEFAULT)
public class DefaultExecContextContainer implements CommonExecContextContainer {

  DefaultExecContext<?> execContext;

  @Override
  @SuppressWarnings("rawtypes")
  public Class<? extends ExecContext> getContextClass() {
    return this.execContext == null ? DefaultExecContext.class : this.execContext.getClass();
  }

  public <D extends Dto> void set(DefaultExecContext<D> execContext) {
    this.execContext = execContext;
  }

  @SuppressWarnings("unchecked")
  public <D extends Dto> DefaultExecContext<D> createContext() {
    this.execContext = new DefaultExecContext<>();
    log.debug("ContextProvider [{}] created context [{}]", this, this.execContext);
    return (DefaultExecContext<D>) this.execContext;
  }

  public <D extends Dto> DefaultExecContext<D> get() {
    //noinspection unchecked
    return (DefaultExecContext<D>) this.execContext;
  }
}
