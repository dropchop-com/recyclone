package com.dropchop.recyclone.base.api.config;

import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Getter
public class HttpConfig {
  private final String applicationName;
  private final String loginUrl;
  private final boolean permissive;

  public HttpConfig(String applicationName, String loginUrl, boolean permissive) {
    this.applicationName = applicationName;
    this.loginUrl = loginUrl;
    this.permissive = permissive;
  }
}
