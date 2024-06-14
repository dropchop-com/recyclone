package com.dropchop.recyclone.quarkus.deployment.rest;

import io.quarkus.arc.processor.AnnotationsTransformer;
import org.jboss.jandex.AnnotationTarget.Kind;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import java.util.Map;

/**
 * Not used ... just here as a sample of how to replace annotations.
 */
public class RestResourceAnnotationProcessor implements AnnotationsTransformer {

  final Map<DotName, String> addAnnotation;

  public RestResourceAnnotationProcessor(Map<DotName, String> addAnnotation) {
    this.addAnnotation = addAnnotation;
  }

  @Override
  public boolean appliesTo(Kind kind) {
    return kind == Kind.CLASS;
  }

  @Override
  public void transform(TransformationContext context) {
    ClassInfo classInfo = context.getTarget().asClass();
    String newPath = addAnnotation.get(classInfo.name());
    /*if (newPath != null) {
      context.transform().add(
          DotName.createSimple(Path.class.getName()),
          AnnotationValue.createStringValue("value", newPath)
      ).add(
          DotName.createSimple(RecycloneResource.class.getName()),
          AnnotationValue.createStringValue("value", newPath)
      ).done();
    }*/
  }
}
