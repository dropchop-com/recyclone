package com.dropchop.recyclone.base.api.service.security.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 23. 03. 2026.
 */
public class UserUuidToken implements AuthenticationToken {
  private final String uuidToken;

  public UserUuidToken(String uuidToken) {
    this.uuidToken = uuidToken;
  }

  @Override
  public Object getPrincipal() {
    return this.uuidToken;
  }

  public String getCredentials() {
    return uuidToken;
  }
}
