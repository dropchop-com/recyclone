package com.dropchop.recyclone.quarkus.deployment.rest;

import io.quarkus.builder.item.SimpleBuildItem;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.MethodInfo;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
public final class RestMappingBuildItem extends SimpleBuildItem {
  private final Map<MethodInfo, RestMethodMapping> methodMapping;
  private final Map<ClassInfo, RestClassMapping> classMapping;

  public RestMappingBuildItem(Map<MethodInfo, RestMethodMapping> methodMapping,
                              Map<ClassInfo, RestClassMapping> classMapping) {
    this.methodMapping = methodMapping;
    this.classMapping = classMapping;
  }

  public Map<MethodInfo, RestMethodMapping> getMethodMapping() {
    return methodMapping;
  }

  public Map<ClassInfo, RestClassMapping> getClassMapping() {
    return classMapping;
  }
}
