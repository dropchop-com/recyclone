package com.dropchop.shiro.filter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
public class BearerConfig extends HttpConfig {
  public BearerConfig(String applicationName, boolean permissive) {
    super(applicationName, null, permissive);
  }
}
