package com.dropchop.recyclone.base.api.config;

import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Getter
public class JwtConfig extends HttpConfig {
  private final String issuer;
  private final String secret;
  private final Integer timeoutSeconds;

  public JwtConfig(String applicationName, String loginUrl, boolean permissive, String issuer,
                   String secret, Integer timeoutSeconds) {
    super(applicationName, loginUrl, permissive);
    this.issuer = issuer;
    this.secret = secret;
    this.timeoutSeconds = timeoutSeconds;
  }
}
