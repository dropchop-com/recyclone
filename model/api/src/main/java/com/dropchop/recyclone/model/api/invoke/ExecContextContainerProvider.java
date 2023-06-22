package com.dropchop.recyclone.model.api.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 09. 22.
 */
public interface ExecContextContainerProvider {
  ExecContextContainer getExecContextProvider(Class<?> execContextClass);

  ExecContextContainer getFirstInitializedExecContextProvider();
}
