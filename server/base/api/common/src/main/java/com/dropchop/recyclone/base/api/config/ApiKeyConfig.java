package com.dropchop.recyclone.base.api.config;

import lombok.Getter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
@Getter
public class ApiKeyConfig extends HttpConfig {
  private final String headerName;
  private final String queryName;

  public ApiKeyConfig(String applicationName, boolean permissive,
                      String headerName, String queryName) {
    super(applicationName, null, permissive);
    this.headerName = headerName;
    this.queryName = queryName;
  }
}
