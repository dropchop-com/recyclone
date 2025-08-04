package com.dropchop.recyclone.quarkus.runtime.config;

import io.quarkus.runtime.annotations.*;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;
import java.util.Optional;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 03. 24.
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
    @WithDefault("com.dropchop.recyclone.base.dto.model.invoke.Params")
    String defaultParams();

    /**
     * Default ExecContext class implementation to be instantiated if can not be determined from code or annotations.
     */
    @WithDefault("com.dropchop.recyclone.base.dto.model.invoke.DefaultExecContext")
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
     * REST Security information.
     */
    Security security();

    /**
     * REST Security information.
     */
    interface Security {
      /**
       * Additional security configuration.
       */
      @ConfigDocSection
      @ConfigDocMapKey("security-name")
      List<Mechanism> mechanisms();

      /**
       * REST OpenAPI security item configuration.
       */
      @ConfigGroup
      interface Mechanism {

        enum MechanismType {
          BEARER_TOKEN("bearer-token"),
          BASIC_AUTH("basic-auth"),
          API_KEY("api-key"),
          JWT("jwt");

          private final String value;

          MechanismType(String value) {
            this.value = value;
          }

          @Override
          public String toString() {
            return String.valueOf(value);
          }
        }

        /**
         * REST OpenAPI security item enabled.
         */
        MechanismType mechanism();

        /**
         * REST OpenAPI security item type (bearer-token, basic-auth, jwt).
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
         * REST security item apiKeyName if the type is apiKey.
         */
        @WithDefault("X-API-Key")
        String headerName();

        /**
         * REST security item apiKeyName if the type is apiKey.
         */
        @WithDefault("api-key")
        String queryName();

        /**
         * REST OpenAPI security item bearerFormat if a scheme is bearer.
         */
        Optional<String> bearerFormat();

        /**
         * REST security item timeout seconds if a scheme is JWT.
         */
        Optional<Integer> timeoutSeconds();

        /**
         * REST security item issuer if a scheme is JWT.
         */
        Optional<String> issuer();

        /**
         * REST security item secret if a scheme is JWT.
         */
        Optional<String> secret();

        /**
         * REST security item loginPath if a scheme is JWT.
         */
        @WithDefault("/api/security/login/jwt")
        String loginPath();

        /**
         * REST security item permissive filter if a scheme is JWT, apiKey, Bearer.
         */
        @WithDefault("true")
        Boolean permissive();
      }
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
     * Dependant injection point configuration.
     */
    @ConfigGroup
    interface Dependant {

      /**
       * Dependency injection point configuration.
       */
      @ConfigGroup
      interface Dependency {

        /**
         * This is the order of evaluation for matching against available implementation class names:
         * <ol>
         * <li>If a match string starts with "^" symbol, then a reg-ex is assumed.</li>
         * <li>If wildcard "*" or "?" is used in a match string, then match glob-like class name.</li>
         * <li>If pattern is prefixed with a "->", then a full class name is assumed and all descendants apply.</li>
         * <li>If match string contains a "." character then match class name by equality.</li>
         * <li>Lastly simple class name equality is matched.</li>
         * </ol>
         */
        String match();

        /**
         * Dependency target <code>com.dropchop.recyclone.base.api.common.RecycloneType</code> qualifier value.
         */
        @WithDefault("<same-as-dependant>")
        String targetQualifier();

        /**
         * Fallback for the dependency
         * <code>com.dropchop.recyclone.base.api.common.RecycloneType</code> qualifier value.
         */
        @WithDefault(RECYCLONE_DEFAULT)
        String fallbackQualifier();
      }

      /**
       * This is the order of evaluation for matching against available implementation class names:
       * <ol>
       * <li>If a match string starts with "^" symbol, then a reg-ex is assumed.</li>
       * <li>If wildcard "*" or "?" is used in a match string, then match glob-like class name.</li>
       * <li>If pattern is prefixed with a "->", then a full class name is assumed and all descendants apply.</li>
       * <li>If match string contains a "." character then match class name by equality.</li>
       * <li>Lastly simple class name equality is matched.</li>
       * </ol>
       */
      String match();

      /**
       * Target's injection point default
       * <code>com.dropchop.recyclone.base.api.common.RecycloneType</code> qualifier value.
       */
      @WithDefault("<same-as-dependant>")
      String targetQualifier();

      /**
       * Fallback for the target's injection point default
       * <code>com.dropchop.recyclone.base.api.common.RecycloneType</code> qualifier value.
       */
      @WithDefault(RECYCLONE_DEFAULT)
      String fallbackQualifier();

      /**
       * List of match rules to match dependencies inside dependants
       */
      List<Dependency> matchDependencies();
    }

    /**
     * Target's injection point default
     * <code>com.dropchop.recyclone.base.api.common.RecycloneType</code> qualifier value.
     */
    @WithDefault("<same-as-dependant>")
    String targetQualifier();

    /**
     * Fallback for the target's injection point default
     * <code>com.dropchop.recyclone.base.api.common.RecycloneType</code> qualifier value.
     */
    @WithDefault(RECYCLONE_DEFAULT)
    String fallbackQualifier();

    /**
     * List of match rules to match dependants, classes that have injection points for Recyclone Layer classes.
     * (i.e.: Resources, Services, Repositories etc ...)
     */
    List<Dependant> matchDependants();
  }

  /**
   * Injection points configuration.
   */
  InjectionPointsConfig injectionPointsConfig();
}
