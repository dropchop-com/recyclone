package com.dropchop.shiro.filter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
public class ApiKeyConfig extends HttpConfig {
  public final String headerName;
  public final String queryName;

  public ApiKeyConfig(String applicationName, boolean permissive,
                      String headerName, String queryName) {
    super(applicationName, null, permissive);
    this.headerName = headerName;
    this.queryName = queryName;
  }
}
