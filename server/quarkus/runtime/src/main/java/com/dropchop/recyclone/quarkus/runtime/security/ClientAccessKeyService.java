package com.dropchop.recyclone.quarkus.runtime.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.security.AccessKey;
import com.dropchop.recyclone.base.api.model.security.ClientKeyConfig;
import com.dropchop.recyclone.base.api.model.security.ClientKeyConfigs;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.ClientAccessKeys;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.ClientAccessKeys.KeyConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class ClientAccessKeyService implements com.dropchop.recyclone.base.api.service.security.ClientAccessKeyService {

  @Inject
  RecycloneBuildConfig recycloneConfig;

  @Override
  @Produces
  @ApplicationScoped
  public ClientKeyConfigs loadAccessKeysConfig() {
    ClientAccessKeys clientAccessKeys = recycloneConfig.rest().security().clientAccessKeys();
    ClientKeyConfigs keyConfigMap = new ClientKeyConfigs();
    for (Map.Entry<String, KeyConfig> configEntry : clientAccessKeys.named().entrySet()) {
      String clientId = configEntry.getKey();
      KeyConfig config = configEntry.getValue();
      ClientKeyConfig clientKeyConfig = new ClientKeyConfig(
          clientId,
          config.queryName(),
          config.headerName(),
          config.expiresAfterSeconds(),
          config.uri().map(URI::create).orElse(null),
          config.secret(),
          config.salt().orElse(null)
      );
      keyConfigMap.put(clientId, clientKeyConfig);
    }
    return keyConfigMap;
  }

  @Override
  public Map<AccessKey, String> createAccessKeys(HasId identifiable, AuthenticationToken token) {
    Map<AccessKey, String> accessKeys = new LinkedHashMap<>();
    for (Map.Entry<String, ClientKeyConfig> configEntry : loadAccessKeysConfig().entrySet()) {
      String clientId = configEntry.getKey();
      ClientKeyConfig clientKeyConfig = configEntry.getValue();
      if (token instanceof UsernamePasswordToken upToken) {
        AccessKey accessKey = new AccessKey(
            clientId,
            ZonedDateTime.now(),
            identifiable.getId(),
            upToken.getUsername(),
            upToken.getPassword()
        );
        String accessEncryptedKey = AccessKey.encrypt(
            clientKeyConfig, accessKey
        );
        accessKeys.put(accessKey, accessEncryptedKey);
      } else if (token instanceof HostAuthenticationToken bearerToken) {
        AccessKey accessKey = new AccessKey(
            clientId,
            ZonedDateTime.now(),
            identifiable.getId(),
            String.valueOf(bearerToken.getCredentials())
        );
        String accessEncryptedKey = AccessKey.encrypt(
            clientKeyConfig, accessKey
        );
        accessKeys.put(accessKey, accessEncryptedKey);
      }
    }
    return accessKeys;
  }
}
