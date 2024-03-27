package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneResource;
import io.quarkus.arc.processor.AnnotationsTransformer;
import jakarta.ws.rs.Path;
import org.jboss.jandex.AnnotationTarget.Kind;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
    if (newPath != null) {
      context.transform().add(
          DotName.createSimple(Path.class.getName()),
          AnnotationValue.createStringValue("value", newPath)
      ).add(
          DotName.createSimple(RecycloneResource.class.getName()),
          AnnotationValue.createStringValue("value", newPath)
      ).done();
    }
  }
}
