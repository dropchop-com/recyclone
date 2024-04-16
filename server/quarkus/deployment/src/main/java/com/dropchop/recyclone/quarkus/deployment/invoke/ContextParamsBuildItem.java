package com.dropchop.recyclone.quarkus.deployment.invoke;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Map;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 04. 24.
 */
public final class ContextParamsBuildItem extends SimpleBuildItem {

  private final Map<String, Integer> contextParamClasses;

  public ContextParamsBuildItem(Map<String, Integer> contextParamClasses) {
    this.contextParamClasses = contextParamClasses;
  }

  public Map<String, Integer> getContextParamClasses() {
    return contextParamClasses;
  }
}
