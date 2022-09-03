package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.invoke.ExecContext;
import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.service.api.ExecContextType;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
@Slf4j
@ApplicationScoped
public class ExecContextProviderProducer {

  @Inject
  @Any
  Instance<ExecContextProvider> execContextProviders;

  @Inject
  @RequestScoped
  @ExecContextType(Constants.Implementation.RCYN_DEFAULT)
  DefaultExecContextProvider defaultExecContextProvider;

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