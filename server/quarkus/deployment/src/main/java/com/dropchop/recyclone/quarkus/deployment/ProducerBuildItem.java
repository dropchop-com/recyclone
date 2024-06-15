package com.dropchop.recyclone.quarkus.deployment;

import io.quarkus.builder.item.SimpleBuildItem;

import java.util.Set;

public final class ProducerBuildItem extends SimpleBuildItem {

  private final Set<ProducerMapping> producerMappings;

  public ProducerBuildItem(Set<ProducerMapping> producerMappings) {
    this.producerMappings = producerMappings;
  }

  public Set<ProducerMapping> getProducerMappings() {
    return producerMappings;
  }
}
