package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import io.quarkus.builder.item.SimpleBuildItem;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
public final class RestMappingBuildItem extends SimpleBuildItem {
  private final RestMapping mapping;

  public RestMappingBuildItem(RestMapping mapping) {
    this.mapping = mapping;
  }

  public RestMapping getMapping() {
    return mapping;
  }
}
