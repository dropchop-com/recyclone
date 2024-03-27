package com.dropchop.recyclone.quarkus.deployment.registry;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 24.
 */
public final class JsonSerializationTypeItem extends SimpleBuildItem {

  private final Collection<String> typeClasses;

  public JsonSerializationTypeItem(Collection<String> typeClasses) {
    this.typeClasses = typeClasses;
  }

  public Collection<String> getClassNames() {
    return typeClasses;
  }

}
