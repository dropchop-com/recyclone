package com.dropchop.recyclone.quarkus.deployment.invoke;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 04. 24.
 */
public final class ParamsBuildItem extends SimpleBuildItem {

  private final Map<String, Integer> classPriorityMap;

  public ParamsBuildItem(Map<String, Integer> classPriorityMap) {
    this.classPriorityMap = classPriorityMap;
  }

  public Map<String, Integer> getClassPriorityMap() {
    return classPriorityMap;
  }
}
