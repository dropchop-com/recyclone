package com.dropchop.recyclone.quarkus.deployment.registry;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 24.
 */
public final class MapperSubTypeItem extends SimpleBuildItem {

  private final Map<String, String> mappingClasses;

  public MapperSubTypeItem(Map<String, String> mappingClasses) {
    this.mappingClasses = mappingClasses;
  }

  public Map<String, String> getClassNames() {
    return mappingClasses;
  }
}
