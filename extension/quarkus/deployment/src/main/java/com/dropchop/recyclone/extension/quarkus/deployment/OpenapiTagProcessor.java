package com.dropchop.recyclone.extension.quarkus.deployment;

import com.dropchop.recyclone.model.api.invoke.CodeParams;
import com.dropchop.recyclone.model.api.rest.Constants;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.gizmo.Gizmo;
import org.jboss.jandex.*;
import org.objectweb.asm.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;

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

  private static final DotName ANNO_VALUE = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.CommonParams"
  );
  private static final DotName ANNO_DATA_CLASS = DotName.createSimple(
      "com.dropchop.recyclone.model.api.base.Dto"
  );
  private static final DotName ANNO_EXEC_CTX_CLASS = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.ExecContext"
  );
  private static final boolean ANNO_INTERNAL = false;


  private static final String TAG_ANNOTATION_DESCRIPTOR = "Lorg/eclipse/microprofile/openapi/annotations/tags/Tag;";
  private static final String PRODUCES_ANNOTATION_DESCRIPTOR = "Ljakarta/ws/rs/Produces;";



  private String extractSecondFromLastPathSegment(AnnotationInstance pathAnnotation) {
    String pathValue = pathAnnotation.value().asString();
    // Assuming path starts with "/", remove it for splitting
    String normalizedPath = pathValue.startsWith("/") ? pathValue.substring(1) : pathValue;
    String[] segments = normalizedPath.split("/");

    // Return the second segment or null if not available
    return segments.length >= 2 ? segments[segments.length - 2] : null;
  }

  @SuppressWarnings("SameParameterValue")
  private DotName getClassAnnotationValue(AnnotationInstance annotation, String property, DotName defaultType) {
    AnnotationValue value = annotation.value(property);
    if (value == null) {
      return null;
    }
    DotName type = value.asClass().name();
    return !type.equals(defaultType) ? type : null;
  }

  @SuppressWarnings("SameParameterValue")
  private boolean getBooleanAnnotationValue(AnnotationInstance annotation, String property, boolean defaultType) {
    AnnotationValue value = annotation.value(property);
    if (value == null) {
      return defaultType;
    }
    return value.asBoolean();
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


  @BuildStep
  public void fakeStep(CombinedIndexBuildItem combinedIndexBuildItem,
                       BuildProducer<BytecodeTransformerBuildItem> transformers) {

    /*BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem(
        true,
        "com.dropchop.recyclone.test.rest.jaxrs.api.DummyResource",
        (ignored, classVisitor) -> new ClassVisitor(Gizmo.ASM_API_VERSION, classVisitor) {
          @Override
          public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (!name.equals("get")) {
              return mv;
            }
            AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
            avTag.visit("name", "internal");
            avTag.visitEnd();
            avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
            avTag.visit("name", "test");
            avTag.visitEnd();
            avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
            avTag.visit("name", "dyn-params:com.dropchop.recyclone.model.api.invoke.CodeParams");
            avTag.visitEnd();
            mv.visitEnd();
            return mv;
          }
        }
    );
    transformers.produce(item);*/
    BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
        .setCacheable(false)
        .setPriority(1)
        .setEager(true)
        .setClassToTransform("com.dropchop.recyclone.test.rest.jaxrs.api.DummyResource")
        .setInputTransformer(
            new BiFunction<>() {
              @Override
              public byte[] apply(String className, byte[] bytes) {
                ClassReader cr = new ClassReader(bytes);
                ClassWriter cw = new ClassWriter(cr, 0);

                // ClassVisitor that adds an annotation to a specific method
                ClassVisitor cv = new ClassVisitor(Gizmo.ASM_API_VERSION, cw) {
                  @Override
                  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor mv;
                    // Check if this is the method to annotate
                    if ("get".equals(name) || "getRest".equals(name)) {
                      mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                      // Add an annotation to the method
                      AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                      avTag.visit("name", "internal");
                      avTag.visitEnd();
                      //mv.visitEnd();
                      /*avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                      avTag.visit("name", "test");
                      avTag.visitEnd();
                      mv.visitEnd();
                      avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                      avTag.visit("name", "dyn-params:com.dropchop.recyclone.model.api.invoke.CodeParams");
                      avTag.visitEnd();
                      mv.visitEnd();*/
                    } else {
                      mv = super.visitMethod(access, name, descriptor, signature, null);
                    }

                    return mv;
                  }
                };
                AnnotationVisitor avTag = cv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                avTag.visit("name", "internal");
                avTag.visitEnd();

                //MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, "get", "()Lcom/dropchop/recyclone/model/dto/rest/Result;", "()Lcom/dropchop/recyclone/model/dto/rest/Result<Lcom/dropchop/recyclone/test/model/dto/Dummy;>;", null);
                //avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                //avTag.visit("name", "internal1");
                //avTag.visitEnd();
                // Read the class and apply the transformation
                cr.accept(cv, ClassReader.SKIP_DEBUG);
                FileOutputStream fos;
                try {
                  fos = new FileOutputStream("/home/nikola/DummyResource.class");
                  fos.write(cw.toByteArray());
                  fos.close();
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
                return cw.toByteArray();
              }
            }
        ).build();

    /*BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem(
        true,
        "com.dropchop.recyclone.test.rest.jaxrs.server.DummyResource",
        (ignored, classVisitor) -> new ClassVisitor(Gizmo.ASM_API_VERSION, classVisitor) {
          @Override
          public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (!name.equals("get")) {
              return mv;
            }
            AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
            avTag.visit("name", "internal");
            avTag.visitEnd();
            avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
            avTag.visit("name", "test");
            avTag.visitEnd();
            avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
            avTag.visit("name", "dyn-params:com.dropchop.recyclone.model.api.invoke.CodeParams");
            avTag.visitEnd();
            mv.visitEnd();
            return mv;
          }
        },
        true
    );*/
    transformers.produce(item);
  }

  /**
   * This method produces OpenAPI org.eclipse.microprofile.openapi.annotations.tags.Tag annotations
   * Example:
   * \@Tag(name = Constants.Tags.TEST)
   * \@Tag(name = Tags.DynamicContext.INTERNAL)
   * \@Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
   */
  //@BuildStep
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
      String pathSegment = extractSecondFromLastPathSegment(pathAnnotation);
      String className = "L" + classInfo.name().toString().replace('.', '/') + ";";

      // Extract and check values from @DynamicExecContext
      DotName valueClassName = getClassAnnotationValue(
          dynamicExecAnnotation, "value", ANNO_VALUE
      );
      /*DotName dataClassName = getClassAnnotationValue(
          dynamicExecAnnotation, "dataClass", DTO
      );
      DotName execContextClassName = getClassAnnotationValue(
          dynamicExecAnnotation, "execContextClass", EXEC_CONTEXT
      );*/
      boolean internal = getBooleanAnnotationValue(dynamicExecAnnotation, "internal", ANNO_INTERNAL);
      Map<String, MethodInfo> augmentation = new HashMap<>();
      for (MethodInfo methodInfo : classInfo.methods()) {
        augmentation.put(methodInfo.genericSignature(), methodInfo);
      }

      if (!classInfo.name().toString().endsWith("DummyResource")) {
        continue;
      }

      transformers.produce(new BytecodeTransformerBuildItem.Builder()
          .setClassToTransform(classInfo.name().toString())
          .setVisitorFunction((ignored, classVisitor) ->
          new ClassVisitor(Gizmo.ASM_API_VERSION, classVisitor) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
              MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
              MethodInfo methodInfo = augmentation.get(signature);
              if (methodInfo == null) {
                return mv;
              }
              if (methodInfo.hasAnnotation(TAG_ANNOTATION)) {
                return mv;
              }
              if (methodInfo.hasAnnotation(TAGS_ANNOTATION)) {
                return mv;
              }
              if (internal) {
                addTagAnnotation(mv, Constants.Tags.DynamicContext.INTERNAL);
              } else {
                addTagAnnotation(mv, Constants.Tags.DynamicContext.PUBLIC);
              }
              if (pathSegment != null && !pathSegment.isBlank()) {
                addTagAnnotation(mv, pathSegment);
              }
              if (!methodInfo.hasAnnotation(GET_ANNOTATION)) {
                return mv;
              }
              addTagAnnotation(mv, "dyn-params:" + valueClassName);
              mv.visitEnd();
              classVisitor.visitEnd();
              return mv;
            }
          }).build()
      );
    }
  }
}
