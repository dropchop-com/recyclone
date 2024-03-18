package com.dropchop.recyclone.quarkus.deployment.rest;

import io.quarkus.builder.item.SimpleBuildItem;
import org.jboss.jandex.MethodInfo;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
public final class RestMappingItem extends SimpleBuildItem {
  private final Map<MethodInfo, RestMapping> mapping;

  public RestMappingItem(Map<MethodInfo, RestMapping> mapping) {
    this.mapping = mapping;
  }

  public Map<MethodInfo, RestMapping> getMapping() {
    return mapping;
  }
}
