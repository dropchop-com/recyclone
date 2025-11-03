package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.quarkus.runtime.elasticsearch.*;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanDefiningAnnotationBuildItem;
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

  @BuildStep
  public void registerInitializer(List<DevServicesResultBuildItem> devServicesResultBuildItems,
                                  BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    if (devServicesResultBuildItems.isEmpty()) {
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
    for (DevServicesResultBuildItem result : devServicesResultBuildItems) {
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
  void registerBeansWhenElasticsearchPresent(BuildProducer<AdditionalBeanBuildItem> additionalBeans,
                                             List<BeanDefiningAnnotationBuildItem> beanDefiningAnnotationBuildItems) {
    for (BeanDefiningAnnotationBuildItem item : beanDefiningAnnotationBuildItems) {
      if (item.getName().toString().equals("io.quarkus.elasticsearch.restclient.lowlevel.ElasticsearchClientConfig")) {
        additionalBeans.produce(
            AdditionalBeanBuildItem
                .builder()
                .addBeanClasses(ElasticConnectionEvictor.class)
                .setUnremovable()
                .build()
        );
        additionalBeans.produce(
            AdditionalBeanBuildItem
                .builder()
                .addBeanClasses(ElasticHttpClientTuning.class)
                .setUnremovable()
                .build()
        );
        log.infof("Registering Elasticsearch Client http connection tuning. Timeout evictor.");
        break;
      }
    }
  }
}
