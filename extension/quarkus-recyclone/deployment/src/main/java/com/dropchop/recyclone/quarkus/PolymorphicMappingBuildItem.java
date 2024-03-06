package com.dropchop.recyclone.quarkus;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 02. 24.
 */
public final class PolymorphicMappingBuildItem extends SimpleBuildItem {

  private final Map<String, String> mappingClasses;

  public PolymorphicMappingBuildItem(Map<String, String> mappingClasses) {
    this.mappingClasses = mappingClasses;
  }

  public Map<String, String> getClassNames() {
    return mappingClasses;
  }
}
