package com.dropchop.recyclone.quarkus.deployment.rest;

import io.quarkus.arc.processor.AnnotationsTransformer;
import org.jboss.jandex.AnnotationTarget.Kind;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("unused")
public class RestResourceRemoveAnnotationProcessor implements AnnotationsTransformer {

  private final Set<DotName> classNamesToTransform;
  private final Collection<DotName> annotationNames;

  public RestResourceRemoveAnnotationProcessor(Set<DotName> classNamesToTransform,
                                               Collection<DotName> annotationNames) {
    this.classNamesToTransform = classNamesToTransform;
    this.annotationNames = annotationNames;
  }

  @Override
  public boolean appliesTo(Kind kind) {
    return kind == Kind.CLASS || kind == Kind.METHOD;
  }

  @Override
  public void transform(TransformationContext context) {
    if (context.getTarget().kind() == Kind.CLASS) {
      ClassInfo classInfo = context.getTarget().asClass();
      if (classNamesToTransform.contains(classInfo.name())) {
        context.transform().remove(
            annotationInstance -> annotationNames.contains(annotationInstance.name())
        ).done();
      }
    } else if (context.getTarget().kind() == Kind.METHOD) {
      MethodInfo methodInfo = context.getTarget().asMethod();
      ClassInfo classInfo = methodInfo.declaringClass();
      if (classNamesToTransform.contains(classInfo.name())) {
        context.transform().remove(
            annotationInstance -> annotationNames.contains(annotationInstance.name())
        ).done();
      }
    }
  }
}
