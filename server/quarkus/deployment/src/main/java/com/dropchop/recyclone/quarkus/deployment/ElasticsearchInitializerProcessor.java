package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticsearchInitializer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.BuildSteps;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import io.quarkus.elasticsearch.restclient.common.deployment.DevservicesElasticsearchBuildItem;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/24/25.
 */
@BuildSteps(onlyIfNot = IsNormal.class, onlyIf = GlobalDevServicesConfig.Enabled.class)
public class ElasticsearchInitializerProcessor {

  @BuildStep
  public void registerInitializer(List<DevservicesElasticsearchBuildItem> devservicesElasticsearchBuildItems,
                   BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    if (devservicesElasticsearchBuildItems.isEmpty()) {
      return;
    }
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ElasticsearchInitializer.class)
            .setUnremovable()
            .build()
    );
  }
}
