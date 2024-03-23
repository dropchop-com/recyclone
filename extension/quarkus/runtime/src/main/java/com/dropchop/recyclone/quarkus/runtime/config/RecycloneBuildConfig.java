package com.dropchop.recyclone.quarkus.runtime.config;

import io.quarkus.runtime.annotations.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@ConfigRoot(name = "recyclone", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused"})
public class RecycloneBuildConfig {

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
     * Additional security configuration.
     */
    @ConfigDocSection
    @ConfigDocMapKey("security-name")
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, Security> security = new LinkedHashMap<>();
    /**
     * REST OpenAPI security item configuration.
     */
    @ConfigGroup
    public static class Security {
      /**
       * REST OpenAPI security item type (apiKey, http, oauth2).
       */
      @ConfigItem
      public Optional<String> type;
      /**
       * REST OpenAPI security item scheme (bearer, basic).
       */
      @ConfigItem
      public Optional<String> scheme;
      /**
       * REST OpenAPI security item location (header, query, cookie).
       */
      @ConfigItem
      public Optional<String> in;
      /**
       * REST OpenAPI security item apiKeyName if type is apiKey.
       */
      @ConfigItem
      public Optional<String> apiKeyName;
      /**
       * REST OpenAPI security item bearerFormat if scheme is bearer.
       */
      @ConfigItem
      public Optional<String> bearerFormat;
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
