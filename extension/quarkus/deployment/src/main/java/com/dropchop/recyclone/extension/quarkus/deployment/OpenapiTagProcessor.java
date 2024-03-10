package com.dropchop.recyclone.extension.quarkus.deployment;

import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.gizmo.Gizmo;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import org.jboss.jandex.*;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
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


  private void addJavassistTagAnnotation(ConstPool constPool, List<AnnotationMemberValue> amvs, String value) {
    Annotation annotation = new Annotation(TAG_ANNOTATION.toString(), constPool);
    annotation.addMemberValue("name", new StringMemberValue(value, constPool));
    amvs.add(new AnnotationMemberValue(annotation, constPool));

  }

  private void addJavassistTagAnnotationsToMethod(ConstPool constPool, CtMethod method,
                                                  List<AnnotationMemberValue> amvs) {
    javassist.bytecode.MethodInfo methodInfo = method.getMethodInfo();
    AnnotationsAttribute attribute = (AnnotationsAttribute) methodInfo
        .getAttribute(AnnotationsAttribute.visibleTag);
    if (attribute == null) {
      attribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
    }

    // Prepare container for repeatable annotations
    Annotation containerAnnotation = new Annotation(TAGS_ANNOTATION.toString(), constPool);
    ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
    arrayMemberValue.setValue(amvs.toArray(AnnotationMemberValue[]::new));
    containerAnnotation.addMemberValue("value", arrayMemberValue);

    attribute.addAnnotation(containerAnnotation);
    methodInfo.addAttribute(attribute);
  }


  /**
   * This method produces OpenAPI org.eclipse.microprofile.openapi.annotations.tags.Tag annotations
   * Example:
   * \@Tag(name = Constants.Tags.TEST)
   * \@Tag(name = Tags.DynamicContext.INTERNAL)
   * \@Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
   * This doesn't work, although it is correctly done...
   */
  @BuildStep
  @SuppressWarnings("DuplicatedCode")
  public void addTagsToMethodsJavassistQuarkus(CombinedIndexBuildItem combinedIndexBuildItem,
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

      if (!classInfo.name().toString().equals("com.dropchop.recyclone.test.rest.jaxrs.api.DummyResource")) {
        continue;
      }

      BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
          .setClassToTransform(classInfo.name().toString())
          .setInputTransformer(
              (className, bytes) -> {
                ClassPool pool = ClassPool.getDefault();
                pool.insertClassPath(new ByteArrayClassPath(className, bytes));
                CtClass ctClass;
                try {
                  ctClass = pool.get(className);
                  CtMethod[] methods = ctClass.getDeclaredMethods();
                  ConstPool constPool = ctClass.getClassFile().getConstPool();
                  for (CtMethod method : methods) {
                    MethodInfo indexInfo = augmentation.get(method.getGenericSignature());
                    javassist.bytecode.MethodInfo methodInfo = method.getMethodInfo();
                    if (indexInfo == null) {
                      continue;
                    }
                    if (indexInfo.hasAnnotation(TAG_ANNOTATION)) {
                      continue;
                    }
                    if (indexInfo.hasAnnotation(TAGS_ANNOTATION)) {
                      continue;
                    }
                    List<AnnotationMemberValue> amvs = new ArrayList<>(5);
                    if (internal) {
                      addJavassistTagAnnotation(constPool, amvs, Tags.DynamicContext.INTERNAL);
                    } else {
                      addJavassistTagAnnotation(constPool, amvs, Tags.DynamicContext.PUBLIC);
                    }
                    if (pathSegment != null && !pathSegment.isBlank()) {
                      addJavassistTagAnnotation(constPool, amvs, pathSegment);
                    }
                    if (!indexInfo.hasAnnotation(GET_ANNOTATION)) {
                      addJavassistTagAnnotationsToMethod(constPool, method, amvs); //write and continue
                      continue;
                    }
                    addJavassistTagAnnotation(
                        constPool, amvs, Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + valueClassName
                    );
                    addJavassistTagAnnotationsToMethod(constPool, method, amvs);
                  }
                  //if (ctClass.isFrozen()) {
                  //  ctClass.defrost();
                  //}
                  byte[] classBytes = ctClass.toBytecode();
                  FileOutputStream fos;
                  try {
                    fos = new FileOutputStream("/Users/nikola/Projects/recyclone/" + className + ".class");
                    fos.write(classBytes);
                    fos.close();
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                  ctClass.detach();
                  return classBytes;
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              }
          ).build();
      transformers.produce(item);
    }
  }

  private void addAsmTagAnnotation(MethodVisitor mv, String tagName) {
    AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
    if (avTag != null) {
      avTag.visit("name", tagName);
      avTag.visitEnd();
    }
  }

  /**
   * Keeping this for quick tests, but it's never used.
   */
  //@BuildStep
  public void addTagsToMethodsJavassist(CombinedIndexBuildItem combinedIndexBuildItem,
                                        BuildProducer<BytecodeTransformerBuildItem> transformers) {
    String cname = "com.dropchop.recyclone.test.rest.jaxrs.api.DummyResource";
    BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
        .setCacheable(false)
        .setPriority(1)
        .setEager(true)
        .setClassToTransform(cname)
        .setInputTransformer(
            (className, bytes) -> {
              ClassPool pool = ClassPool.getDefault();
              pool.insertClassPath(new ByteArrayClassPath(className, bytes));
              CtClass ctClass;
              try {
                ctClass = pool.get(className);
                CtMethod[] methods = ctClass.getDeclaredMethods();
                for (CtMethod method : methods) {
                  if (method.getName().equals("get") || method.getName().equals("getRest")) {
                    // Create an annotation to add to the method
                    ConstPool constPool = ctClass.getClassFile().getConstPool();
                    AnnotationsAttribute attribute = (AnnotationsAttribute) method
                        .getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
                    if (attribute == null) {
                      attribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                    }
                    Annotation annotation = new Annotation(TAG_ANNOTATION.toString(), constPool); // Adjust the annotation name
                    annotation.addMemberValue("name", new StringMemberValue("Example", constPool));
                    attribute.addAnnotation(annotation);
                    method.getMethodInfo().addAttribute(attribute);
                  }
                }
                byte[] classBytes = ctClass.toBytecode();
                FileOutputStream fos;
                try {
                  fos = new FileOutputStream("/Users/nikola/Projects/recyclone/DummyResource.class");
                  fos.write(classBytes);
                  fos.close();
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
                return classBytes;
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }
        ).build();
    transformers.produce(item);
  }

  /**
   * This should work also, but it does not.
   * (if you check decompiled class file)
   */
  //@BuildStep
  public void addTagsToMethodsAsmLowLevelDefunct(CombinedIndexBuildItem combinedIndexBuildItem,
                                                 BuildProducer<BytecodeTransformerBuildItem> transformers) {
    BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
        .setCacheable(false)
        .setPriority(1)
        .setEager(true)
        .setClassToTransform("com.dropchop.recyclone.test.rest.jaxrs.api.DummyResource")
        .setInputTransformer(
            (className, bytes) -> {
                ClassReader cr = new ClassReader(bytes);
                ClassWriter cw = new ClassWriter(cr, 0);

                // ClassVisitor that adds an annotation to a specific method
                ClassVisitor cv = new ClassVisitor(Gizmo.ASM_API_VERSION, cw) {
                  @Override
                  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                    // Check if this is the method to annotate
                    if ("get".equals(name) || "getRest".equals(name)) {
                      // Add an annotation to the method
                      AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                      avTag.visit("name", "internalX");
                      avTag.visitEnd();
                      //mv.visitEnd();
                      /*avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                      avTag.visit("name", "test");
                      avTag.visitEnd();
                      avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                      avTag.visit("name", "dyn-params:com.dropchop.recyclone.model.api.invoke.CodeParams");
                      avTag.visitEnd();*/
                    }
                    mv.visitEnd();
                    return mv;
                  }
                };
                AnnotationVisitor avTag = cv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
                avTag.visit("name", "internal2");
                avTag.visitEnd();
                cr.accept(cv, 0);
                FileOutputStream fos;
                try {
                  fos = new FileOutputStream("/home/nikola/projects/recyclone/DummyResource.class");
                  fos.write(cw.toByteArray());
                  fos.close();
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
                return cw.toByteArray();
            }
        ).build();
    transformers.produce(item);
  }

  /**
   * This method produces OpenAPI org.eclipse.microprofile.openapi.annotations.tags.Tag annotations
   * Example:
   * \@Tag(name = Constants.Tags.TEST)
   * \@Tag(name = Tags.DynamicContext.INTERNAL)
   * \@Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
   * This doesn't work, although it is correctly done...
   */
  //@BuildStep
  @SuppressWarnings("DuplicatedCode")
  public void addTagsToMethodsAsmQuarkusDefunct(CombinedIndexBuildItem combinedIndexBuildItem,
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
                addAsmTagAnnotation(mv, Tags.DynamicContext.INTERNAL);
              } else {
                addAsmTagAnnotation(mv, Tags.DynamicContext.PUBLIC);
              }
              if (pathSegment != null && !pathSegment.isBlank()) {
                addAsmTagAnnotation(mv, pathSegment);
              }
              if (!methodInfo.hasAnnotation(GET_ANNOTATION)) {
                return mv;
              }
              addAsmTagAnnotation(mv, "dyn-params:" + valueClassName);
              mv.visitEnd();
              classVisitor.visitEnd();
              return mv;
            }
          }).build()
      );
    }
  }
}
