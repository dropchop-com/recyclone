package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.quarkus.runtime.elasticsearch.DefaultInitializerSignaler;
import com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticSearchTestHelper;
import com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticsearchInitializer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.DevServicesResultBuildItem;
import org.jboss.logging.Logger;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/24/25.
 */
//@BuildSteps(onlyIfNot = IsNormal.class, onlyIf = DevServicesConfig.Enabled.class)
public class ElasticsearchProcessor {

  private static final Logger log = Logger.getLogger(ElasticsearchProcessor.class);

  private static final String CAP_ELASTICSEARCH_REST = "io.quarkus.elasticsearch.rest-client";
  private static final String CAP_ELASTICSEARCH_JAVA = "io.quarkus.elasticsearch.java-client";

  @BuildStep
  public void registerInitializer(List<DevServicesResultBuildItem> devservicesElasticsearchBuildItems,
                                  BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    if (devservicesElasticsearchBuildItems.isEmpty()) {
      log.info("Registering cache initializer DefaultInitializerSignaler.");
      additionalBeanBuildItemProducer.produce(
          AdditionalBeanBuildItem
              .builder()
              .addBeanClasses(DefaultInitializerSignaler.class)
              .setUnremovable()
              .build()
      );
      return;
    }
    for (DevServicesResultBuildItem result : devservicesElasticsearchBuildItems) {
      if (result.getName().startsWith("elasticsearch")) {
        log.infof("Registering Elasticsearch initializer for: %s@%s", result.getName(), result.getConfig());
        additionalBeanBuildItemProducer.produce(
            AdditionalBeanBuildItem
                .builder()
                .addBeanClasses(ElasticsearchInitializer.class)
                .setUnremovable()
                .build()
        );
        additionalBeanBuildItemProducer.produce(
            AdditionalBeanBuildItem
                .builder()
                .addBeanClasses(ElasticSearchTestHelper.class)
                .setUnremovable()
                .build()
        );
      }
    }
  }

  @BuildStep
  void registerBeansWhenElasticsearchPresent(Capabilities capabilities,
                                             BuildProducer<AdditionalBeanBuildItem> additionalBeans) {

    boolean hasEs = capabilities.isPresent(CAP_ELASTICSEARCH_REST)
        || capabilities.isPresent(CAP_ELASTICSEARCH_JAVA);

    if (!hasEs) {
      return;
    }

    additionalBeans.produce(AdditionalBeanBuildItem.builder()
        .addBeanClass("com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticConnectionEvictor")
        .addBeanClass("com.dropchop.recyclone.quarkus.runtime.elasticsearch.ElasticHttpClientTuning")
        .setUnremovable()
        .build());
  }
}
