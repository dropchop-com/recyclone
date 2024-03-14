package com.dropchop.recyclone.quarkus.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

import java.util.List;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@ConfigRoot(name = "recyclone", phase = ConfigPhase.RUN_TIME)
public class RecycloneAppConfig {

  /**
   * Configuration for REST resources.
   */
  @ConfigItem
  public Optional<RestConfig> rest;

  /**
   * Configuration for REST resource implementation classes.
   */
  @ConfigGroup
  public static class RestConfig {

    /**
     * REST resource implementation classes inclusion.
     */
    @ConfigItem
    public Optional<List<String>> includes;

    /**
     * REST resource implementation classes exclusion.
     */
    @ConfigItem
    public Optional<List<String>> excludes;
  }
}
