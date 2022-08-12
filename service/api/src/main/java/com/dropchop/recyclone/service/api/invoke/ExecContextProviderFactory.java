package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.marker.Constants;
import com.dropchop.recyclone.service.api.ExecContextType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
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
        return execContextProvider;
      }
    }
    return defaultExecContextProvider;
  }
}
