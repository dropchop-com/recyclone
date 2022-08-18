package com.dropchop.recyclone.service.api.invoke;

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
public class ExecContextProviderFactory {

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
        log.debug("Returning provider [{}] for context [{}].", execContextProvider, execContextClass);
        return execContextProvider;
      }
    }
    log.debug("Returning default provider [{}] for context [{}].", defaultExecContextProvider, execContextClass);
    return defaultExecContextProvider;
  }
}
