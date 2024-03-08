package com.dropchop.recyclone.extension.quarkus.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class RecycloneProcessor {

  private static final String FEATURE = "quarkus-recyclone";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }
}
