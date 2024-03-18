package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneResource;
import io.quarkus.arc.processor.AnnotationsTransformer;
import org.jboss.jandex.AnnotationTarget.Kind;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import java.util.Set;

public class RestResourceAnnotationProcessor implements AnnotationsTransformer {

  private final Set<DotName> classNamesToTransform;

  public RestResourceAnnotationProcessor(Set<DotName> classNamesToTransform) {
    this.classNamesToTransform = classNamesToTransform;
  }

  @Override
  public boolean appliesTo(Kind kind) {
    return kind == Kind.CLASS;
  }

  @Override
  public void transform(TransformationContext context) {
    ClassInfo classInfo = context.getTarget().asClass();
    if (classNamesToTransform.contains(classInfo.name())) {
      context.transform().add(DotName.createSimple(RecycloneResource.class.getName())).done();
    }
  }
}
