package com.dropchop.shiro.token;

import org.apache.shiro.authc.HostAuthenticationToken;

public class UuidApiKeyToken implements HostAuthenticationToken {

  private final String uuidToken;

  public UuidApiKeyToken(String uuidToken) {
    this.uuidToken = uuidToken;
  }

  @Override
  public Object getPrincipal() {
    return this.uuidToken;
  }

  @Override
  public String getHost() {
    return "";
  }

  public String getCredentials() {
    return uuidToken;
  }
}
