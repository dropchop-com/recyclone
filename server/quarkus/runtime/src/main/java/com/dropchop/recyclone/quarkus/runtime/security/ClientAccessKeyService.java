package com.dropchop.recyclone.quarkus.runtime.security;

import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.config.ClientKeyConfig;
import com.dropchop.recyclone.base.dto.model.security.LoginAccount;
import com.dropchop.recyclone.base.dto.model.security.User;
import com.dropchop.recyclone.base.dto.model.security.UserAccount;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.ClientAccessKeys;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest.Security.ClientAccessKeys.KeyConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
  public Map<String, ClientKeyConfig> loadAccessKeysConfig() {
    ClientAccessKeys clientAccessKeys = recycloneConfig.rest().security().clientAccessKeys();
    Map<String, ClientKeyConfig> keyConfigMap = new LinkedHashMap<>();
    for (Map.Entry<String, KeyConfig> configEntry : clientAccessKeys.named().entrySet()) {
      String clientId = configEntry.getKey();
      KeyConfig config = configEntry.getValue();
      ClientKeyConfig clientKeyConfig = new ClientKeyConfig(clientId, config.secret(), config.salt().orElse(null));
      keyConfigMap.put(clientId, clientKeyConfig);
    }
    return keyConfigMap;
  }

  @Override
  public void loadAccessKeys(User user) {
    for (Map.Entry<String, ClientKeyConfig> configEntry : loadAccessKeysConfig().entrySet()) {
      String clientId = configEntry.getKey();
      ClientKeyConfig clientKeyConfig = configEntry.getValue();
      for (UserAccount account : user.getAccounts()) {
        if (account instanceof LoginAccount loginAccount) {
          String accessKey = AccessKey.encrypt(
              clientKeyConfig, loginAccount.getLoginName(), loginAccount.getPassword()
          );
          user.setAttributeValue(clientId + ".accessKey", accessKey);
        }
      }
    }
  }
}
