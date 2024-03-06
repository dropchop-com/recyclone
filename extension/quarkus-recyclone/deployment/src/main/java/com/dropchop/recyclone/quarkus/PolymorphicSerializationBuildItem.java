package com.dropchop.recyclone.quarkus;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Map;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 02. 24.
 */
public final class PolymorphicSerializationBuildItem extends SimpleBuildItem {
  private final Map<String, String> typeClasNameMap;

  public PolymorphicSerializationBuildItem(Map<String, String> typeClasNameMap) {
    this.typeClasNameMap = typeClasNameMap;
  }

  public Map<String, String> getClassNames() {
    return typeClasNameMap;
  }
}
