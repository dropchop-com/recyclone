package com.dropchop.recyclone.base.api.config;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
public class JwtConfig extends HttpConfig {
  public final String issuer;
  public final String secret;
  public final Integer timeoutSeconds;

  public JwtConfig(String applicationName, String loginUrl, boolean permissive, String issuer,
                   String secret, Integer timeoutSeconds) {
    super(applicationName, loginUrl, permissive);
    this.issuer = issuer;
    this.secret = secret;
    this.timeoutSeconds = timeoutSeconds;
  }
}
