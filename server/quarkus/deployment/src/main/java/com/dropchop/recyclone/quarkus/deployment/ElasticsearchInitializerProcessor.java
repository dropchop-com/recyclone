package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticsearchInitializer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.IsNormal;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.BuildSteps;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import io.quarkus.deployment.dev.devservices.GlobalDevServicesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/24/25.
 */
@BuildSteps(onlyIfNot = IsNormal.class, onlyIf = GlobalDevServicesConfig.Enabled.class)
public class ElasticsearchInitializerProcessor {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchInitializerProcessor.class);

  @BuildStep
  public void registerInitializer(List<DevServicesResultBuildItem> devservicesElasticsearchBuildItems,
                                  BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    if (devservicesElasticsearchBuildItems.isEmpty()) {
      return;
    }
    for (DevServicesResultBuildItem result : devservicesElasticsearchBuildItems) {
      if (result.getName().startsWith("elasticsearch")) {
        log.info("Registering Elasticsearch initializer for: {}@{}", result.getName(), result.getConfig());
        additionalBeanBuildItemProducer.produce(
            AdditionalBeanBuildItem
                .builder()
                .addBeanClasses(ElasticsearchInitializer.class)
                .setUnremovable()
                .build()
        );
      }
    }
  }
}
