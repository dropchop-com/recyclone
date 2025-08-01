package com.dropchop.recyclone.base.api.config;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
public class HttpConfig {
  public final String applicationName;
  public final String loginUrl;
  public final boolean permissive;

  public HttpConfig(String applicationName, String loginUrl, boolean permissive) {
    this.applicationName = applicationName;
    this.loginUrl = loginUrl;
    this.permissive = permissive;
  }
}
