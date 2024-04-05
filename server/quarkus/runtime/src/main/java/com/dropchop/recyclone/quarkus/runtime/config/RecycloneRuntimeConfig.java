package com.dropchop.recyclone.quarkus.runtime.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@ConfigRoot(name = "recyclone", phase = ConfigPhase.RUN_TIME)
public class RecycloneRuntimeConfig {

  /**
   * Test value.
   */
  @ConfigItem
  public Optional<String> test;
}
