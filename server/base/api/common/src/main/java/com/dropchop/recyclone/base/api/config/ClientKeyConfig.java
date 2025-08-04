package com.dropchop.recyclone.base.api.config;

import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 04. 08. 2025
 */
@Getter
public class ClientKeyConfig {
  private final String clientId;
  private final String secret;
  private final String salt;

  public ClientKeyConfig(String clientId, String secret, String salt) {
    this.clientId = clientId;
    this.secret = secret;
    this.salt = salt;
  }
}
