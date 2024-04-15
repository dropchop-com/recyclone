package com.dropchop.recyclone.quarkus.runtime.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@ConfigMapping(prefix = "quarkus.recyclone")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
interface RecycloneRuntimeConfig {

  /**
   * Test value.
   */
  Optional<String> test();
}
