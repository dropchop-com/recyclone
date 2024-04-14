package com.dropchop.recyclone.quarkus.deployment.service;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Map;

public final class ServiceBuildItem extends SimpleBuildItem {

  private final Map<String, String> services;

  public ServiceBuildItem(Map<String, String> services) {
    this.services = services;
  }

  public Map<String, String> getServices() {
    return services;
  }
}
