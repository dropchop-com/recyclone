package com.dropchop.recyclone.quarkus.runtime.config;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithParentName;

import java.util.Map;
import java.util.Optional;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 03. 24.
 */
@ConfigMapping(prefix = "quarkus.recyclone")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface RecycloneRuntimeConfig {

  /**
   * REST Runtime information.
   */
  Rest rest();

  interface Rest {

    /**
     * REST Runtime Security information.
     */
    Security security();

    interface Security {
      /**
       * REST runtime client access key information.
       */
      ClientAccessKeys clientAccessKeys();

      @ConfigGroup
      interface ClientAccessKeys {

        /**
         * REST client-id named access key configs.
         */
        @WithParentName
        Map<String, KeyConfig> named();

        /**
         * REST client-id named access key configs.
         */
        @ConfigGroup
        interface KeyConfig {

          /**
           * REST secret for the named access key config.
           */
          Optional<String> secret();

          /**
           * REST salt for the named access key config.
           */
          Optional<String> salt();

          /**
           * REST uri for the named access key config.
           */
          Optional<String> uri();

          /**
           * REST uri for the named access key config.
           */
          @WithDefault("3600")
          Integer expiresAfterSeconds();

          /**
           * REST security item apiKeyName if the type is apiKey.
           */
          @WithDefault("X-Client-API-Key")
          String headerName();

          /**
           * REST security item apiKeyName if the type is apiKey.
           */
          @WithDefault("client-api-key")
          String queryName();
        }
      }
    }
  }
}
