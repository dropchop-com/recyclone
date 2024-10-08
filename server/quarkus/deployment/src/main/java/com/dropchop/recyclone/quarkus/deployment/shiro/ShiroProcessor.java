package com.dropchop.recyclone.quarkus.deployment.shiro;

import com.dropchop.shiro.cdi.DefaultShiroEnvironmentProvider;
import com.dropchop.shiro.cdi.ShiroAuthenticationService;
import com.dropchop.shiro.cdi.ShiroAuthorizationService;
import com.dropchop.shiro.cdi.ShiroEnvironment;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.resteasy.reactive.server.spi.AnnotationsTransformerBuildItem;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.util.function.Consumer;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 03. 24.
 */
public class ShiroProcessor {

  private static final Logger LOG = Logger.getLogger(ShiroProcessor.class);

  private static final DotName DC_PERMISSION_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.model.api.security.annotations.RequiresPermissions"
  );

  private static final DotName SHIRO_PERMISSION_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresPermissions"
  );

  private static final DotName DC_ROLE_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.model.api.security.annotations.RequiresRoles"
  );

  private static final DotName SHIRO_ROLE_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresRoles"
  );

  private static final DotName DC_USER_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.model.api.security.annotations.RequiresUser"
  );

  private static final DotName SHIRO_USER_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresUser"
  );

  private static final DotName DC_GUEST_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.model.api.security.annotations.RequiresGuest"
  );

  private static final DotName SHIRO_GUEST_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresGuest"
  );

  private static final DotName DC_AUTH_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.model.api.security.annotations.RequiresAuthentication"
  );

  private static final DotName SHIRO_AUTH_ANNO = DotName.createSimple(
      "org.apache.shiro.authz.annotation.RequiresAuthentication"
  );

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

  private AnnotationInstance copyAnnoValues(Declaration declaration, DotName oldAnno, DotName newAnno) {
    AnnotationInstance oldAnnotation = declaration.annotation(oldAnno);
    AnnotationValue[] annoValues = oldAnnotation.values().toArray(new AnnotationValue[]{});
    LOG.infof("Replacing annotation new [%s -> %s] in [%s]", oldAnno, newAnno, declaration);
    return AnnotationInstance.create(
        newAnno,
        declaration,
        annoValues
    );
  }

  Consumer<AnnotationTransformation.TransformationContext> getTransformationContextConsumer() {
    return context -> {
      Declaration declaration = context.declaration();
      if (declaration.hasAnnotation(DC_PERMISSION_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_PERMISSION_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_PERMISSION_ANNO, SHIRO_PERMISSION_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_ROLE_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_ROLE_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_ROLE_ANNO, SHIRO_ROLE_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_USER_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_USER_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_USER_ANNO, SHIRO_USER_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_GUEST_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_GUEST_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_GUEST_ANNO, SHIRO_GUEST_ANNO
        ));
      } else if (declaration.hasAnnotation(DC_AUTH_ANNO)) {
        context.remove(ann -> ann.name().equals(DC_AUTH_ANNO));
        context.add(copyAnnoValues(
            declaration, DC_AUTH_ANNO, SHIRO_AUTH_ANNO
        ));
      }
    };
  }

  //@BuildStep //not working
  void transformShiroAnnotation(
      BuildProducer<GeneratedBeanBuildItem> generatedBeans,
      BuildProducer<AnnotationsTransformerBuildItem> restTransformer,
      BuildProducer<io.quarkus.resteasy.reactive.server.spi.AnnotationsTransformerBuildItem> arcTransformer) {
    restTransformer.produce(
        new AnnotationsTransformerBuildItem(AnnotationTransformation.builder()
          .whenDeclaration(d -> d.hasAnnotation(DC_PERMISSION_ANNO)
              || d.hasAnnotation(DC_ROLE_ANNO) || d.hasAnnotation(DC_USER_ANNO)
              || d.hasAnnotation(DC_GUEST_ANNO) || d.hasAnnotation(DC_AUTH_ANNO))
          .transform(getTransformationContextConsumer())
        )
    );

    /*arcTransformer.produce(
        new AnnotationsTransformerBuildItem(AnnotationTransformation.builder()
          .whenDeclaration(d -> d.hasAnnotation(DC_PERMISSION_ANNO)
              || d.hasAnnotation(DC_ROLE_ANNO) || d.hasAnnotation(DC_USER_ANNO)
              || d.hasAnnotation(DC_GUEST_ANNO) || d.hasAnnotation(DC_AUTH_ANNO))
          .transform(getTransformationContextConsumer())
        )
    );*/
  }
}
