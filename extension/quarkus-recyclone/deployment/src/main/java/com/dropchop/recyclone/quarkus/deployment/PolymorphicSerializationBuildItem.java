package com.dropchop.recyclone.quarkus.deployment;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 02. 24.
 */
public final class PolymorphicSerializationBuildItem extends SimpleBuildItem {
  private final Set<String> classNames;

  public PolymorphicSerializationBuildItem(Set<String> classNames) {
    this.classNames = classNames;
  }

  public Set<String> getClassNames() {
    return classNames;
  }
}
