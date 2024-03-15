package com.dropchop.recyclone.quarkus.runtime.spi;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 24.
 */
@ApplicationScoped
public class RestResourceConfigProvider {

  private final RestResourceConfig config;

  public RestResourceConfigProvider(RestResourceConfig config) {
    this.config = config;
  }

  public RestResourceConfig getConfig() {
    return this.config;
  }
}
