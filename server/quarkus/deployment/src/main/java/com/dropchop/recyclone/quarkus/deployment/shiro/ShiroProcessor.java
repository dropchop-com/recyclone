package com.dropchop.recyclone.quarkus.deployment.shiro;

import com.dropchop.shiro.cdi.DefaultShiroEnvironmentProvider;
import com.dropchop.shiro.cdi.ShiroAuthenticationService;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import com.dropchop.shiro.cdi.ShiroEnvironment;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 27. 03. 24.
 */
public class ShiroProcessor {

  @BuildStep
  public void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(DefaultShiroEnvironmentProvider.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ShiroEnvironment.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ShiroAuthorizationService.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ShiroAuthenticationService.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
  }

  /*@BuildStep
  public void registerProviders(BuildProducer<DynamicFeatureBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        new DynamicFeatureBuildItem(ShiroDynamicFeature.class.getName(), true)
    );
  }*/
}
