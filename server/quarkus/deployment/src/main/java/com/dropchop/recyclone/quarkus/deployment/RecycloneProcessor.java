package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.quarkus.runtime.invoke.*;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ExecContextPropertyFilterSerializerModifier;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ObjectMapperFactory;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ParamsFactoryDeserializerModifier;
import com.dropchop.recyclone.quarkus.runtime.service.ServiceSelector;
import com.dropchop.recyclone.quarkus.runtime.spi.bean.RecycloneApplicationImpl;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
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
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-rest-server"));
    indexDependency.produce(new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-mapper-api"));
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
            .addBeanClasses(ExecContextSelector.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ParamsSelector.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextBinder.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextContainer.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(CommonExecContextContainer.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ParamsExecContextContainer.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ParamsFactoryDeserializerModifier.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextPropertyFilterSerializerModifier.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ObjectMapperFactory.class)
            .setUnremovable()
            .build()
    );
  }
}
