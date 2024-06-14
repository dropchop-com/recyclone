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
   * Configuration for REST implementation.
   */
  @ConfigGroup
  interface Rest {

    /**
     * REST base path.
     */
    Optional<String> path();

    /**
     * Default Params class implementation to be instantiated if can not be determined from code or annotations.
     */
    @WithDefault("com.dropchop.recyclone.model.dto.invoke.Params")
    String defaultParams();

    /**
     * Default ExecContext class implementation to be instantiated if can not be determined from code or annotations.
     */
    @WithDefault("com.dropchop.recyclone.model.dto.invoke.DefaultExecContext")
    String defaultExecContext();

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
     * we support API interface class name matching with:
     *   - glob pattern with * and ?
     *   - regex pattern if expr starts with ^ character
     */
    Optional<List<String>> includes();

    /**
     * REST resource implementation classes exclusion.
     */
    Optional<List<String>> excludes();
  }

  /**
   * Configuration for REST resources.
   */
  Rest rest();

  /**
   * Root injection points configuration.
   */
  @ConfigGroup
  interface InjectionPointsConfig {

    /**
     * Single dependant class injection point configuration.
     */
    @ConfigGroup
    interface Dependant {

      /**
       * Single injection point configuration.
       */
      @ConfigGroup
      interface Dependency {

        /**
         * if wildcard is used in match string then match glob class name,
         * if match string starts with ^ reg-ex is assumed,
         * if match string is full class name then match class name, or if prefixed with "->" all descendants apply
         * lastly simple class name is matched
         */
        //@WithParentName
        String match();

        /**
         * Target's injection point default @Named qualifier value.
         */
        @WithDefault("<same-as-dependant>")
        String targetQualifier();

        /**
         * Fallback for the target's injection point default @Named qualifier value.
         */
        @WithDefault("recyclone_default")
        String fallbackQualifier();
      }

      /**
       * if wildcard is used in match string then match glob class name,
       * if match string starts with ^ reg-ex is assumed,
       * if match string is full class name then match class name, or if prefixed with "->" all descendants apply
       * lastly simple class name is matched
       */
      //@WithParentName
      String match();

      /**
       * Target's injection point default @Named qualifier value.
       */
      @WithDefault("<same-as-dependant>")
      String targetQualifier();

      /**
       * Fallback for the target's injection point default @Named qualifier value.
       */
      @WithDefault("recyclone_default")
      String fallbackQualifier();

      /**
       * List of match rules to match dependencies inside dependants
       */
      List<Dependency> matchDependencies();
    }

    /**
     * Target's injection point default @Named qualifier value.
     */
    @WithDefault("<same-as-dependant>")
    String targetQualifier();

    /**
     * Fallback for the target's injection point default @Named qualifier value.
     */
    @WithDefault("recyclone_default")
    String fallbackQualifier();

    /**
     * List of match rules to match dependants.
     */
    List<Dependant> matchDependants();
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

  /**
   * Injection points configuration.
   */
  InjectionPointsConfig injectionPointsConfig();
}
