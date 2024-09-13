package com.dropchop.recyclone.quarkus.deployment.invoke;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 04. 24.
 */
public final class ContextsBuildItem extends SimpleBuildItem {

  Set<ContextMapping> contextMappings = new LinkedHashSet<>();

  public ContextsBuildItem(Set<ContextMapping> contextMappings) {
    this.contextMappings = contextMappings;
  }

  public Set<ContextMapping> getContextMappings() {
    return contextMappings;
  }
}
