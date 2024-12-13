package com.dropchop.recyclone.quarkus.deployment;
/*
import rest.com.dropchop.recyclone.base.api.model.Constants.Tags;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.builditem.BytecodeTransformerBuildItem;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.gizmo.Gizmo;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.jboss.jandex.*;
import org.objectweb.asm.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


<dependency>
  <groupId>org.javassist</groupId>
  <artifactId>javassist</artifactId>
  <version>3.29.2-GA</version>
</dependency>

<!-- for ASM way -->
<dependency>
  <groupId>io.quarkus.gizmo</groupId>
  <artifactId>gizmo</artifactId>
  <version>1.8.0</version>
</dependency>

*/

@SuppressWarnings({"unused", "DanglingJavadoc", "CommentedOutCode"})
public class IgnoreMeProcessor {

  /*private static final DotName DYNAMIC_EXEC_CONTEXT = DotName.createSimple(
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
      "invoke.com.dropchop.recyclone.base.api.model.CommonParams"
  );
  private static final DotName ANNO_DATA_CLASS = DotName.createSimple(
      "base.com.dropchop.recyclone.base.api.model.Dto"
  );
  private static final DotName ANNO_EXEC_CTX_CLASS = DotName.createSimple(
      "invoke.com.dropchop.recyclone.base.api.model.ExecContext"
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
  }*/


  /**
   * This method produces OpenAPI org.eclipse.microprofile.openapi.annotations.tags.Tag annotations
   * Example:
   * \@Tag(name = Constants.Tags.TEST)
   * \@Tag(name = Tags.DynamicContext.INTERNAL)
   * \@Tag(name = Tags.DYNAMIC_PARAMS + Tags.DYNAMIC_DELIM + "com.dropchop.recyclone.model.dto.invoke.CodeParams")
   * This doesn't work, although it is correctly done...
   */
  //@BuildStep
  /*@SuppressWarnings("DuplicatedCode")
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
      //DotName dataClassName = getClassAnnotationValue(
      //    dynamicExecAnnotation, "dataClass", DTO
      //);
      //DotName execContextClassName = getClassAnnotationValue(
      //    dynamicExecAnnotation, "execContextClass", EXEC_CONTEXT
      //);
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
                    fos = new FileOutputStream(
                        "%s/projects/recyclone/%s.class".formatted(
                            System.getProperty("user.home"),
                            className
                        )
                    );
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
  }*/

//
//  private void addAsmTagAnnotation(MethodVisitor mv, String tagName) {
//    AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
//    if (avTag != null) {
//      avTag.visit("name", tagName);
//      avTag.visitEnd();
//    }
//  }
//
//  /**
//   * This should work also, but it does not.
//   * (if you check decompiled class file)
//   */
//  @BuildStep
//  public void addTagsToMethodsAsmLowLevelDefunct(CombinedIndexBuildItem combinedIndexBuildItem,
//                                                 BuildProducer<BytecodeTransformerBuildItem> transformers) {
//    BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
//        .setCacheable(false)
//        .setPriority(1)
//        .setEager(true)
//        .setClassToTransform("com.dropchop.recyclone.test.rest.jaxrs.api.DummyResource")
//        .setInputTransformer(
//            (className, bytes) -> {
//                ClassReader cr = new ClassReader(bytes);
//                ClassWriter cw = new ClassWriter(cr, 0);
//
//                // ClassVisitor that adds an annotation to a specific method
//                ClassVisitor cv = new ClassVisitor(Gizmo.ASM_API_VERSION, cw) {
//                  @Override
//                  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//                    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
//                    // Check if this is the method to annotate
//                    if ("get".equals(name) || "getRest".equals(name)) {
//                      // Add an annotation to the method
//                      AnnotationVisitor avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
//                      avTag.visit("name", "internalX");
//                      avTag.visitEnd();
//                      //mv.visitEnd();
//                      /*avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
//                      avTag.visit("name", "test");
//                      avTag.visitEnd();
//                      avTag = mv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
//                      avTag.visit("name", "dyn-params:invoke.com.dropchop.recyclone.base.api.model.CodeParams");
//                      avTag.visitEnd();*/
//                    }
//                    mv.visitEnd();
//                    return mv;
//                  }
//                };
//                AnnotationVisitor avTag = cv.visitAnnotation(TAG_ANNOTATION_DESCRIPTOR, true);
//                avTag.visit("name", "internal2");
//                avTag.visitEnd();
//                cr.accept(cv, 0);
//                FileOutputStream fos;
//                try {
//                  fos = new FileOutputStream("/home/nikola/projects/recyclone/DummyResource.class");
//                  fos.write(cw.toByteArray());
//                  fos.close();
//                } catch (IOException e) {
//                  throw new RuntimeException(e);
//                }
//                return cw.toByteArray();
//            }
//        ).build();
//    transformers.produce(item);
//  }

  /*@BuildStep
  public void registerPathAnnotation(
      BuildProducer<AdditionalJaxRsResourceDefiningAnnotationBuildItem> annotationProducer) {
    annotationProducer.produce(
        new AdditionalJaxRsResourceDefiningAnnotationBuildItem(
            DotName.createSimple(RecycloneResource.class)
        )
    );
  }*/

  /*@BuildStep
  public void processPathAnnotation(
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
      BuildProducer<AnnotationsTransformerBuildItem> transformerProducer) {

    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        continue;
      }
      if (mapping.implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) { // keep defined/desired path annotation
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.implClass.name().toString()
      ).build());
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.apiClass.name().toString()
      ).build());
      AnnotationInstance implPathAnnotation = mapping.implClass.declaredAnnotation(PATH_ANNOTATION);
      if (implPathAnnotation != null) {
        continue;
      }
      String newPath = mapping.internal ? "/internal" + mapping.path : "/public" + mapping.path;
      transformerProducer.produce(
          new AnnotationsTransformerBuildItem(
              new AnnotationsTransformer.ClassTransformerBuilder()
                  .whenClass(o -> o.name().equals(mapping.implClass.name()))
                  .thenTransform(
                      transformation -> transformation.add(
                          DotName.createSimple(Path.class.getName()),
                          AnnotationValue.createStringValue("value", newPath)
                      )
                  )
          )
      );
    }
  }*/

  /*@BuildStep
  public void processPathAnnotation( // transformation called
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
      BuildProducer<AnnotationsTransformerBuildItem> transformerProducer) {

    Map<DotName, String> addAnnotation = new LinkedHashMap<>();
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        continue;
      }
      if (mapping.implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) { // keep defined/desired path annotation
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.implClass.name().toString()
      ).build());
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.apiClass.name().toString()
      ).build());
      AnnotationInstance implPathAnnotation = mapping.implClass.declaredAnnotation(PATH_ANNOTATION);
      if (implPathAnnotation == null) {
        String newPath = mapping.internal ? "/internal" + mapping.path : "/public" + mapping.path;
        addAnnotation.put(mapping.implClass.name(), newPath);
      }
    }

    transformerProducer.produce(
        new AnnotationsTransformerBuildItem(new RestResourceAnnotationProcessor(addAnnotation))
    );
  }*/

  /*@BuildStep
  void addPathAnnotation( // javassist HardCore
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
      BuildProducer<BytecodeTransformerBuildItem> transformers) {
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        continue;
      }
      if (mapping.implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) { // keep defined/desired path annotation
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.implClass.name().toString()
      ).build());
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.apiClass.name().toString()
      ).build());
      AnnotationInstance implPathAnnotation = mapping.implClass.declaredAnnotation(PATH_ANNOTATION);
      if (implPathAnnotation != null) {
        continue;
      }
      String newPath = mapping.internal ? "/internal" + mapping.path : "/public" + mapping.path;
      BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
          .setClassToTransform(mapping.implClass.name().toString())
          .setInputTransformer(
              (className, bytes) -> {
                  ClassPool classPool = ClassPool.getDefault();
                  try {
                    CtClass ctClass = classPool.get(className.replace('/', '.'));
                    AnnotationsAttribute attr = new AnnotationsAttribute(
                        ctClass.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag
                    );
                    Annotation ann = new Annotation(
                        jakarta.ws.rs.Path.class.getName(), ctClass.getClassFile().getConstPool()
                    );
                    ann.addMemberValue(
                        "value",
                        new StringMemberValue(newPath, ctClass.getClassFile().getConstPool())
                    );
                    attr.addAnnotation(ann);
                    ctClass.getClassFile().addAttribute(attr);
                    ctClass.detach(); // Detach the CtClass object from the ClassPool

                    //if (ctClass.isFrozen()) {
                    //  ctClass.defrost();
                    //}
                    byte[] classBytes = ctClass.toBytecode();
                    FileOutputStream fos;
                    try {
                      fos = new FileOutputStream(
                          "%s/projects/recyclone/%s.class".formatted(
                              System.getProperty("user.home"),
                              className
                          )
                      );
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
  }*/
}
