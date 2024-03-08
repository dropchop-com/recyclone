package com.dropchop.recyclone.extension.quarkus.deployment;

import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.rest.jaxrs.api.MediaType;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.gizmo.Gizmo;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;
import org.objectweb.asm.*;

import java.util.Set;

import static jdk.nio.zipfs.ZipFileAttributeView.AttrID.method;

public class OpenapiTagProcessor {

  private static final DotName DYNAMIC_EXEC_CONTEXT = DotName.createSimple(
      "com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext"
  );

  private static final DotName PATH_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.Path"
  );

  private static final DotName GET_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.GET"
  );

  private static final DotName PRODUCES_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.Produces"
  );

  private static final DotName TAG_ANNOTATION = DotName.createSimple(
      "org.eclipse.microprofile.openapi.annotations.tags.Tag"
  );
 private static final DotName TAGS_ANNOTATION = DotName.createSimple(
      "org.eclipse.microprofile.openapi.annotations.tags.Tags"
  );

  private static final DotName COMMON_PARAMS = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.CommonParams"
  );
  private static final DotName DTO = DotName.createSimple(
      "com.dropchop.recyclone.model.api.base.Dto"
  );
  private static final DotName EXEC_CONTEXT = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.ExecContext"
  );


  private static final String TAG_ANNOTATION_DESCRIPTOR = "Lorg/eclipse/microprofile/openapi/annotations/tags/Tag;";
  private static final String PRODUCES_ANNOTATION_DESCRIPTOR = "Ljakarta/ws/rs/Produces;";

  private String extractSecondFromLastPathSegment(String pathValue) {
    // Assuming path starts with "/", remove it for splitting
    String normalizedPath = pathValue.startsWith("/") ? pathValue.substring(1) : pathValue;
    String[] segments = normalizedPath.split("/");

    // Return the second segment or null if not available
    return segments.length >= 2 ? segments[segments.length - 2] : null;
  }

  private DotName getClassAnnotationValue(AnnotationInstance annotation, String property, DotName defaultType) {
    DotName type = annotation.value(property).asClass().name();
    return !type.equals(defaultType) ? type : null;
  }

  private boolean isMethodADynamicParamsCandidate(MethodInfo method) {
    if (!method.hasAnnotation(GET_ANNOTATION)) {
      return false;
    }
    /*if (method.parameters().size() != 1) {
      return true;
    }
    MethodParameterInfo paramType = method.parameters().get(0);
    // Check if parameter type does not equal the 'value' of @DynamicExecContext
    return !paramType.asClass().name().equals(valueClassName);*/
    return true;
  }

  private void addTagAnnotation(MethodVisitor mv, String tagName) {
    AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
    if (avTag != null) {
      avTag.visit("name", tagName);
      avTag.visitEnd();
    }
  }


  private static class AddTagsClassVisitor extends ClassVisitor {

    private static final Logger LOG = Logger.getLogger(AddTagsClassVisitor.class);

    private final String className;
    private final Set<String> methodsWithBridges;

    public RemoveBridgeMethodsClassVisitor(ClassVisitor visitor, String className, Set<String> methodsWithBridges) {
      super(Gizmo.ASM_API_VERSION, visitor);

      this.className = className;
      this.methodsWithBridges = methodsWithBridges;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
      if (methodsWithBridges.contains(name) && ((access & Opcodes.ACC_BRIDGE) != 0)
          && ((access & Opcodes.ACC_SYNTHETIC) != 0)) {



        return null;
      }

      return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
  }

  /**
   * This method produces OpenAPI org.eclipse.microprofile.openapi.annotations.tags.Tag annotations
   * Example:
   * \@Tag(name = Constants.Tags.TEST)
   * \@Tag(name = Tags.DynamicContext.INTERNAL)
   * \@Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
   */
  @BuildStep
  public void addTagsToMethods(CombinedIndexBuildItem combinedIndexBuildItem,
                               BuildProducer<BytecodeTransformerBuildItem> transformers) {
    IndexView index = combinedIndexBuildItem.getIndex();

    // Find classes annotated with @DynamicExecContext
    for (AnnotationInstance dynamicExecAnnotation : index.getAnnotations(DYNAMIC_EXEC_CONTEXT)) {
      if (dynamicExecAnnotation.target().kind() != AnnotationTarget.Kind.CLASS) {
        continue;
      }

      ClassInfo classInfo = dynamicExecAnnotation.target().asClass();
      // Check for @Path annotation
      AnnotationInstance pathAnnotation = classInfo.declaredAnnotation(PATH_ANNOTATION);
      if (pathAnnotation == null) {
        continue;
      }
      String pathValue = pathAnnotation.value().asString();
      String pathSegment = extractSecondFromLastPathSegment(pathValue);
      String className = classInfo.name().toString().replace('.', '/');

      // Extract and check values from @DynamicExecContext
      DotName valueClassName = getClassAnnotationValue(
          dynamicExecAnnotation, "value", COMMON_PARAMS
      );
      /*DotName dataClassName = getClassAnnotationValue(
          dynamicExecAnnotation, "dataClass", DTO
      );
      DotName execContextClassName = getClassAnnotationValue(
          dynamicExecAnnotation, "execContextClass", EXEC_CONTEXT
      );*/
      System.out.println("" + classInfo.name());
      boolean tmp = false;
      try {
        tmp = dynamicExecAnnotation.value("internal").asBoolean();
      } catch (Exception e) {
        System.out.println("" + classInfo.name());
      }
      boolean internal = tmp;

      transformers.produce(new BytecodeTransformerBuildItem(className, (className1, classVisitor) -> {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        // Process each method of the class
        for (MethodInfo methodInfo : classInfo.methods()) {
          // Check if the method already has @Tag annotation
          if (methodInfo.hasAnnotation(TAG_ANNOTATION)) {
            continue;
          }
          if (methodInfo.hasAnnotation(TAGS_ANNOTATION)) {
            continue;
          }
          boolean missingProduces = !methodInfo.hasAnnotation(PRODUCES_ANNOTATION);
          boolean dynParamsCandidate = methodInfo.hasAnnotation(GET_ANNOTATION);

          builder = builder.visit(new AsmVisitorWrapper.ForDeclaredMethods()
              .method(ElementMatchers.named(methodInfo.name()),
                  MethodVisitorWrapper.ForAnnotation.of(AnnotationDescription.Builder.ofType(Tag.class).define("value", "yourTagValueHere").build())));
          classVisitor.visitMethod()
          MethodVisitor mv = classVisitor.visitMethod(access, name, descriptor, signature, exceptions);
        }
        byte[] modifiedClass = builder.make().getBytes();
        return modifiedClass;
      }));
      /*transformers.produce(new BytecodeTransformerBuildItem(className, (className1, classVisitor) -> {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        return new ClassVisitor(Opcodes.ASM9, cw) {
          @Override
          public MethodVisitor visitMethod(
              int access, String name, String descriptor, String signature, String[] exceptions
          ) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (methodInfo.name().equals(name) && methodInfo.descriptor().equals(descriptor)) {
              // Add multiple @Tag annotations
              if (internal) {
                addTagAnnotation(mv, Constants.Tags.DynamicContext.INTERNAL);
              } else {
                addTagAnnotation(mv, Constants.Tags.DynamicContext.PUBLIC);
              }
              if (pathSegment != null && !pathSegment.isBlank()) {
                addTagAnnotation(mv, pathSegment);
              }
              if (dynParamsCandidate) {
                addTagAnnotation(
                    mv, Constants.Tags.DYNAMIC_PARAMS + Constants.Tags.DYNAMIC_DELIM + valueClassName
                );
              }
              if (missingProduces) {
                AnnotationVisitor avTag = mv.visitAnnotation(PRODUCES_ANNOTATION_DESCRIPTOR, true);
                if (avTag != null) {
                  if (methodInfo.name().endsWith("Rest")) {
                    avTag.visit("value", MediaType.APPLICATION_JSON);
                  } else {
                    avTag.visit("value", MediaType.APPLICATION_JSON_DROPCHOP_RESULT);
                  }
                  avTag.visitEnd();
                }
              }
            }
            return mv;
          }
        };
      }));*/

    }
  }
}
