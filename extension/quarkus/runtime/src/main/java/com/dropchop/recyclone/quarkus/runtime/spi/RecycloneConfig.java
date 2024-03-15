package com.dropchop.recyclone.quarkus.runtime.spi;

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
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class RecycloneConfig {

  /**
   * Configuration for REST resources.
   */
  @ConfigItem
  public Rest rest;

  /**
   * Configuration for REST implementation.
   */
  @ConfigGroup
  public static class Rest {

    /**
     * REST base path.
     */
    @ConfigItem
    public Optional<String> path;


    /**
     * REST OpenAPI information.
     */
    @ConfigItem
    public Info info;


    /**
     * REST OpenAPI information
     */
    @ConfigGroup
    public static class Info {

      /**
       * REST OpenAPI information title.
       */
      @ConfigItem
      public Optional<String> title;

      /**
       * REST OpenAPI information version.
       */
      @ConfigItem
      public Optional<String> version;

      /**
       * REST OpenAPI information contact.
       */
      @ConfigItem
      public Contact contact;

      /**
       * REST OpenAPI information license.
       */
      @ConfigItem
      public License license;


      /**
       * REST OpenAPI information contact.
       */
      @ConfigGroup
      public static class Contact {
        /**
         * REST OpenAPI information contact name.
         */
        @ConfigItem
        public Optional<String> name;

        /**
         * REST OpenAPI information contact url.
         */
        @ConfigItem
        public Optional<String> url;

        /**
         * REST OpenAPI information contact url.
         */
        @ConfigItem
        public Optional<String> email;
      }

      /**
       * REST OpenAPI information license.
       */
      @ConfigGroup
      public static class License {
        /**
         * REST OpenAPI information license name.
         */
        @ConfigItem
        public Optional<String> name;

        /**
         * REST OpenAPI information license url.
         */
        @ConfigItem
        public Optional<String> url;
      }
    }
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
