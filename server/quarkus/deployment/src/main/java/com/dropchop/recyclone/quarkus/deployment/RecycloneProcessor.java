package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextProvider;
import com.dropchop.recyclone.quarkus.runtime.invoke.ParamsProvider;
import com.dropchop.recyclone.quarkus.runtime.service.ServiceSelector;
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
    indexDependency.produce(new IndexDependencyBuildItem("org.apache.shiro", "shiro-core"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-model-api"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-model-dto"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-api"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-api-internal"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-server-rest-jackson"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-server-rest-jaxrs"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-repo-api"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-service-api"));
  }

  @BuildStep
  public void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(RecycloneApplicationImpl.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ServiceSelector.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ParamsProvider.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextProvider.class)
            .setUnremovable()
            .build()
    );
  }
}
