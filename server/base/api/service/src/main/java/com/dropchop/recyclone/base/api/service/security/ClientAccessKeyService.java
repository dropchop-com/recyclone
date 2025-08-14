package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.security.AccessKey;
import com.dropchop.recyclone.base.api.model.security.ClientKeyConfigs;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
public interface ClientAccessKeyService {
  ClientKeyConfigs loadAccessKeysConfig();
  Map<AccessKey, String> createAccessKeys(HasId identifiable, AuthenticationToken token);
}
