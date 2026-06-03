package com.dropchop.recyclone.quarkus.runtime.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.security.AccessKey;
import com.dropchop.recyclone.base.api.model.security.ClientKeyConfig;
import com.dropchop.recyclone.base.api.model.security.ClientKeyConfigs;
import com.dropchop.recyclone.base.api.service.security.shiro.UserUuidToken;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneRuntimeConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneRuntimeConfig.Rest.Security.ClientAccessKeys;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneRuntimeConfig.Rest.Security.ClientAccessKeys.KeyConfig;
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
  RecycloneRuntimeConfig recycloneConfig;

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
          config.secret().orElse(null),
          config.salt().orElse(null)
      );
      keyConfigMap.put(clientId, clientKeyConfig);
    }
    return keyConfigMap;
  }

  protected EncryptedAccessKey createAccessKey(String clientKeyId, ClientKeyConfig config,
                                               HasId identifiable, String loginName, char[] secret) {
    AccessKey accessKey = new AccessKey(
        clientKeyId,
        ZonedDateTime.now(),
        identifiable.getId(),
        loginName,
        secret
    );
    if (config.getSecret() != null && config.getSalt() != null) {
      String accessEncryptedKey = AccessKey.encrypt(
          config, accessKey
      );
      return new EncryptedAccessKey(accessKey, accessEncryptedKey);
    }
    throw new IllegalArgumentException("Missing config secret or salt!");
  }

  protected EncryptedAccessKey createAccessKey(String clientKeyId, ClientKeyConfig config,
                                               HasId identifiable, String token) {
    AccessKey accessKey = new AccessKey(
        clientKeyId,
        ZonedDateTime.now(),
        identifiable.getId(),
        token
    );
    if (config.getSecret() != null && config.getSalt() != null) {
      String accessEncryptedKey = AccessKey.encrypt(
          config, accessKey
      );
      return new EncryptedAccessKey(accessKey, accessEncryptedKey);
    } else {
      return new EncryptedAccessKey(accessKey, config.getSecret());
    }
  }

  protected EncryptedAccessKey createAccessKey(String clientKeyId, ClientKeyConfig config,
                                               HasId identifiable, AuthenticationToken token) {
    if (token instanceof UsernamePasswordToken upToken) {
      return this.createAccessKey(clientKeyId, config, identifiable, upToken.getUsername(), upToken.getPassword());
    } else if (token instanceof HostAuthenticationToken || token instanceof UserUuidToken) {
      return this.createAccessKey(clientKeyId, config, identifiable, String.valueOf(token.getCredentials()));
    } else {
      throw new IllegalArgumentException("Unsupported authentication token type: " + token.getClass().getName());
    }
  }

  @Override
  public EncryptedAccessKey createAccessKey(String clientKeyId, String configName, HasId identifiable,
                                String loginName, char[] secret) {
    ClientKeyConfig config = loadAccessKeysConfig().get(configName);
    if (config == null) {
      throw new IllegalArgumentException("Client key configuration not found for name: " + configName);
    }
    return this.createAccessKey(clientKeyId, config, identifiable, loginName, secret);
  }

  @Override
  public EncryptedAccessKey createAccessKey(String clientKeyId, String configName, HasId identifiable, String token) {
    ClientKeyConfig config = loadAccessKeysConfig().get(configName);
    if (config == null) {
      throw new IllegalArgumentException("Client key configuration not found for name: " + configName);
    }
    return this.createAccessKey(clientKeyId, config, identifiable, token);
  }

  @Override
  public Map<AccessKey, String> createAccessKeys(HasId identifiable, AuthenticationToken token) {
    Map<AccessKey, String> accessKeys = new LinkedHashMap<>();
    for (Map.Entry<String, ClientKeyConfig> configEntry : loadAccessKeysConfig().entrySet()) {
      String clientKeyId = configEntry.getKey();
      ClientKeyConfig clientKeyConfig = configEntry.getValue();
      EncryptedAccessKey encryptedAccessKey = this.createAccessKey(clientKeyId, clientKeyConfig, identifiable, token);
      accessKeys.put(encryptedAccessKey.accessKey(), encryptedAccessKey.encryptedKey());
    }
    return accessKeys;
  }
}
