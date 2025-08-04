package com.dropchop.recyclone.base.api.service.security;

import com.dropchop.recyclone.base.api.config.ClientKeyConfig;
import com.dropchop.recyclone.base.dto.model.security.User;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
public interface ClientAccessKeyService {
  Map<String, ClientKeyConfig> loadAccessKeysConfig();
  void loadAccessKeys(User user);
}
