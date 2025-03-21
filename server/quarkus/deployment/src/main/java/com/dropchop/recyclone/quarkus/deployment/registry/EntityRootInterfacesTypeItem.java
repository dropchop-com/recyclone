package com.dropchop.recyclone.quarkus.deployment.registry;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 24.
 */
public final class EntityRootInterfacesTypeItem extends SimpleBuildItem {

  private final Collection<String> typeClasses;

  public EntityRootInterfacesTypeItem(Collection<String> typeClasses) {
    this.typeClasses = typeClasses;
  }

  public Collection<String> getClassNames() {
    return typeClasses;
  }

}
