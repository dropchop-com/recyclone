package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainerProvider;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.service.api.ExecContextType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
@Slf4j
@ApplicationScoped
public class DefaultExecContextContainerProvider implements ExecContextContainerProvider {

  @Inject @Any
  Instance<ExecContextContainer> execContextProviders;

  @Inject
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  DefaultExecContextContainer defaultExecContextProvider;

  @Override
  public ExecContextContainer getExecContextProvider(Class<?> execContextClass) {
    for (ExecContextContainer execContextProvider : execContextProviders) {
      Class<?> clazz = execContextProvider.getContextClass();
      if (clazz.equals(execContextClass)) {
        log.trace("Returning provider [{}] for context [{}].", execContextProvider, execContextClass);
        return execContextProvider;
      }
    }
    log.trace("Returning default provider [{}] for context [{}].", defaultExecContextProvider, execContextClass);
    return defaultExecContextProvider;
  }

  @Override
  public ExecContextContainer getFirstInitializedExecContextProvider() {
    for (ExecContextContainer execContextProvider : execContextProviders) {
      ExecContext<?> context = execContextProvider.get();
      if (context != null) {
        log.trace("Returning initialized provider [{}].", execContextProvider);
        return execContextProvider;
      }
    }
    log.trace("Returning initialized provider [{}].", defaultExecContextProvider);
    ExecContext<?> context = defaultExecContextProvider.get();
    if (context == null) {
      return null;
    }
    return defaultExecContextProvider;
  }
}
