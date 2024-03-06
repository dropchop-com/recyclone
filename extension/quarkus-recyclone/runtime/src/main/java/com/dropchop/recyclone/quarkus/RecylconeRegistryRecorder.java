package com.dropchop.recyclone.quarkus;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 02. 24.
 */
@Recorder
public class RecylconeRegistryRecorder {
  public RuntimeValue<RecylconeRegistryService> createRegistry(RecylconeRegistryService registryService) {
    return new RuntimeValue<>(registryService);
  }
}
