package com.dropchop.recyclone.quarkus.deployment.invoke;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 04. 24.
 */
public final class ExecContextBuildItem extends SimpleBuildItem {

  private final Map<String, String> contextParamClasses;

  public ExecContextBuildItem(Map<String, String> contextParamClasses) {
    this.contextParamClasses = contextParamClasses;
  }

  public Map<String, String> getContextParamClasses() {
    return contextParamClasses;
  }
}
