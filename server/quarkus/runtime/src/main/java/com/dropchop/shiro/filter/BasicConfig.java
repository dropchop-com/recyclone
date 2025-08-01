package com.dropchop.shiro.filter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
public class BasicConfig extends HttpConfig {
  public BasicConfig(String applicationName, boolean permissive) {
    super(applicationName, null, permissive);
  }
}
