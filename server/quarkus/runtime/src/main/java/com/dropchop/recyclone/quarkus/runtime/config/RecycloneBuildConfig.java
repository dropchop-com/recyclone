package com.dropchop.recyclone.quarkus.runtime.config;

import io.quarkus.runtime.annotations.*;
import io.smallrye.config.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Service.DEFAULT_SERVICE;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@ConfigMapping(prefix = "quarkus.recyclone")
@ConfigRoot(phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
@SuppressWarnings({"unused"})
public interface RecycloneBuildConfig {

  /**
   * Configuration for REST resources.
   */
  Rest rest();

  /**
   * Configuration for REST implementation.
   */
  @ConfigGroup
  interface Rest {

    /**
     * REST base path.
     */
    Optional<String> path();

    /**
     * REST OpenAPI information.
     */
    Info info();

    /**
     * REST OpenAPI information
     */
    interface Info {
      /**
       * REST OpenAPI information title.
       */
      Optional<String> title();
      /**
       * REST OpenAPI information version.
       */
      Optional<String> version();
      /**
       * REST OpenAPI information contact.
       */
      Contact contact();
      /**
       * REST OpenAPI information license.
       */
      License license();

      /**
       * REST OpenAPI information contact.
       */
      interface Contact {
        /**
         * REST OpenAPI information contact name.
         */
        Optional<String> name();

        /**
         * REST OpenAPI information contact url.
         */
        Optional<String> url();

        /**
         * REST OpenAPI information contact url.
         */
        Optional<String> email();
      }

      /**
       * REST OpenAPI information license.
       */
      @ConfigGroup
      interface License {
        /**
         * REST OpenAPI information license name.
         */
        Optional<String> name();
        /**
         * REST OpenAPI information license url.
         */
        Optional<String> url();
      }
    }

    /**
     * Additional security configuration.
     */
    @ConfigDocSection
    @ConfigDocMapKey("security-name")
    Map<String, Security> security();

    /**
     * REST OpenAPI security item configuration.
     */
    @ConfigGroup
    interface Security {
      /**
       * REST OpenAPI security item type (apiKey, http, oauth2).
       */
      Optional<String> type();
      /**
       * REST OpenAPI security item scheme (bearer, basic).
       */
      Optional<String> scheme();
      /**
       * REST OpenAPI security item location (header, query, cookie).
       */
      Optional<String> in();
      /**
       * REST OpenAPI security item apiKeyName if type is apiKey.
       */
      Optional<String> apiKeyName();
      /**
       * REST OpenAPI security item bearerFormat if scheme is bearer.
       */
      Optional<String> bearerFormat();
    }

    /**
     * REST resource implementation classes inclusion.
     */
    Optional<List<String>> includes();

    /**
     * REST resource implementation classes exclusion.
     */
    Optional<List<String>> excludes();
  }

  /**
   * Single service configuration.
   */
  interface Service {

    String DEFAULT_SERVICE = "<default>";

    /**
     * Service qualifier name.
     */
    @WithDefault("recyclone_jpa_default")
    String qualifier();
  }

  /**
   * Services configuration.
   */
  @ConfigDocMapKey("service-name")
  @WithDefaults
  //@WithParentName
  @WithUnnamedKey(DEFAULT_SERVICE)
  Map<String, Service> service();
}
