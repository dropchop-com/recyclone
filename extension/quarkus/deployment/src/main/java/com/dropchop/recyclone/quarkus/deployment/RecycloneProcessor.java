package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.quarkus.runtime.spi.bean.RecycloneApplicationImpl;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;

class RecycloneProcessor {

  private static final String FEATURE = "quarkus-recyclone";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  void addDependencies(BuildProducer<IndexDependencyBuildItem> indexDependency) {
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-model-api"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-model-dto"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-api"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-jackson-server"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-jaxrs-api"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-jaxrs-api-intern"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-jaxrs-server"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-repo-api"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-service-api"));
  }
}
