package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.model.api.invoke.ExecContextProviderProducer;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.service.api.ExecContextType;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
@Slf4j
@ApplicationScoped
public class DefaultContextProviderProducer implements ExecContextProviderProducer {

  @Inject
  @Any
  Instance<ExecContextProvider> execContextProviders;

  @Inject
  @RequestScoped
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  DefaultExecContextProvider defaultExecContextProvider;

  @Override
  public ExecContextProvider getExecContextProvider(Class<?> execContextClass) {
    for (ExecContextProvider execContextProvider : execContextProviders) {
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
  public ExecContextProvider getFirstInitializedExecContextProvider() {
    for (ExecContextProvider execContextProvider : execContextProviders) {
      ExecContext<?> context = execContextProvider.produce();
      if (context != null) {
        log.trace("Returning initialized provider [{}].", execContextProvider);
        return execContextProvider;
      }
    }
    log.trace("Returning initialized provider [{}].", defaultExecContextProvider);
    ExecContext<?> context = defaultExecContextProvider.produce();
    if (context == null) {
      return null;
    }
    return defaultExecContextProvider;
  }
}
