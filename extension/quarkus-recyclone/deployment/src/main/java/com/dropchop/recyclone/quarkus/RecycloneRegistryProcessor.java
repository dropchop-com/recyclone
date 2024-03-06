package com.dropchop.recyclone.quarkus;

import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;
import org.jboss.jandex.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

class RecycloneRegistryProcessor {

  private static final String FEATURE = "quarkus-recyclone";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }


  private void processMappingAnnotation(AnnotationInstance annotation, Map<String, String> mappingClasses) {
    AnnotationValue targetValue = annotation.value("target");

    String targetClassName = null;
    if (targetValue != null && targetValue.kind() == AnnotationValue.Kind.CLASS) {
      Type targetType = targetValue.asClass();
      targetClassName = targetType.name().toString();
    }

    AnnotationValue sourceValue = annotation.value("source");
    String sourceClassName = null;
    if (sourceValue != null && sourceValue.kind() == AnnotationValue.Kind.CLASS) {
      Type sourceType = sourceValue.asClass();
      sourceClassName = sourceType.name().toString();
    }

    if (sourceClassName != null && targetClassName != null) {
      mappingClasses.put(sourceClassName, targetClassName);
    }
  }

  @BuildStep
  void collectSubclassMappingAnnotatedClasses(CombinedIndexBuildItem combinedIndexBuildItem,
                                              BuildProducer<PolymorphicMappingBuildItem> classProducer) {
    IndexView index = combinedIndexBuildItem.getIndex();
    Map<String, String> mappingClasses = new LinkedHashMap<>();
    for (AnnotationInstance annotation : index.getAnnotations("org.mapstruct.SubclassMapping")) {
      processMappingAnnotation(annotation, mappingClasses);
    }
    for (AnnotationInstance annotation : index.getAnnotations("org.mapstruct.SubclassMappings")) {
      AnnotationInstance[] subclassMappingAnnotations = annotation.value().asNestedArray();
      if (subclassMappingAnnotations != null) {
        for (AnnotationInstance subclassMappingAnnotation : subclassMappingAnnotations) {
          processMappingAnnotation(subclassMappingAnnotation, mappingClasses);
        }
      }
    }

    classProducer.produce(
        new PolymorphicMappingBuildItem(
            mappingClasses
        )
    );
  }


  @BuildStep
  void collectSerializationAnnotatedClasses(CombinedIndexBuildItem combinedIndexBuildItem,
                                            BuildProducer<PolymorphicSerializationBuildItem> classProducer) {
    IndexView index = combinedIndexBuildItem.getIndex();
    Map<String, String> propertyMappedClasses = new LinkedHashMap<>();
    for (AnnotationInstance annotation : index.getAnnotations("com.fasterxml.jackson.annotation.JsonTypeInfo")) {
      if (annotation.target().kind() == AnnotationTarget.Kind.CLASS
          && annotation.value("property") != null
          && "type".equals(annotation.value("property").asString())) {
        ClassInfo classInfo = annotation.target().asClass();

        AnnotationValue property = annotation.value("property");
        String propertyValue = null;
        if (property != null && property.kind() == AnnotationValue.Kind.STRING) {
          propertyValue = property.asString();
        }
        if (propertyValue != null && propertyValue.equals("type") && classInfo != null) {
          for (ClassInfo subClassInfo : index.getAllKnownSubclasses(classInfo.name())) {
              propertyMappedClasses.put(subClassInfo.simpleName(), subClassInfo.name().toString());
          }
        }
      }
    }
    classProducer.produce(
        new PolymorphicSerializationBuildItem(
            propertyMappedClasses
        )
    );
  }

  @Record(STATIC_INIT)
  @BuildStep
  SyntheticBeanBuildItem setupRuntimeBeans(RecylconeRegistryRecorder recorder,
                                           PolymorphicSerializationBuildItem serializationBuildItem,
                                           PolymorphicMappingBuildItem mappingBuildItems) {
    RecylconeRegistryService registryService = new RecylconeRegistryService();
    for (Map.Entry<String, String> item : mappingBuildItems.getClassNames().entrySet()) {
      registryService.addMappingClassMap(item.getKey(), item.getValue());
    }
    for (Map.Entry<String, String> item : serializationBuildItem.getClassNames().entrySet()) {
      registryService.addSerializationTypeMap(item.getKey(), item.getValue());
    }
    return SyntheticBeanBuildItem.configure(RecylconeRegistryService.class)
        .scope(Singleton.class)
        .runtimeValue(recorder.createRegistry(registryService))
        .done();
  }

    /*@BuildStep
    void configureRuntimeBean(BuildProducer<RuntimeInitializedClassBuildItem> runtimeInit,
                              BuildProducer<SyntheticBeanBuildItem> syntheticBean,
                              PolymorphicSerializationBuildItem serializationBuildItem,
                              PolymorphicMappingBuildItem mappingBuildItems) {

        syntheticBean.produce(
            SyntheticBeanBuildItem.configure(RecylconeRegistryService.class)
            .scope(ApplicationScoped.class)
            .setRuntimeInit()
            .supplier(() -> {
                RecylconeRegistryService service = new RecylconeRegistryService();
                for (Map.Entry<String, String> item : mappingBuildItems.getClassNames().entrySet()) {
                    service.addMappingClassNames(item.getKey(), item.getValue());
                }
                for (String item : serializationBuildItem.getClassNames()) {
                    service.addSerializationClassName(item);
                }
                return service;
            }).done());
    }*/


    /*@BuildStep
    GeneratedBeanBuildItem createCollectableClassService(IndexView index,
                                                         BuildProducer<ReflectiveClassBuildItem> reflectiveClasses) {
        Set<String> classnames = new HashSet<>();
        index.getAnnotations(COLLECTABLE).forEach(annotationInstance -> {
            classnames.add(annotationInstance.target().asClass().name().toString());
            reflectiveClasses.produce(new ReflectiveClassBuildItem(true, true, annotationInstance.target().asClass().name().toString()));
        });

        ClassOutput classOutput = new GeneratedBeanGizmoAdaptor();
        try (ClassCreator classCreator = ClassCreator.builder().classOutput(classOutput)
            .className("com.example.runtime.CollectableClassService")
            .superClass(Object.class)
            .interfaces(java.io.Serializable.class)
            .build()) {

            classCreator.addAnnotation(ApplicationScoped.class.getName());

            // Field holding class names
            FieldCreator fieldCreator = classCreator.getFieldCreator("classnames", Set.class.getName())
                .setModifiers(Modifier.PRIVATE);

            // Constructor
            try (MethodCreator constructor = classCreator.getMethodCreator("<init>", void.class)) {
                constructor.setModifiers(Modifier.PUBLIC);
                constructor.invokeSpecialMethod(MethodDescriptor.ofMethod(Object.class, "<init>", void.class), constructor.getThis());
                constructor.writeInstanceField(fieldCreator.getFieldDescriptor(), constructor.getThis(),
                    constructor.load(classnames));
                constructor.returnValue(null);
            }

            // Getter for classnames
            try (MethodCreator getter = classCreator.getMethodCreator("getClassnames", Set.class)) {
                getter.setModifiers(Modifier.PUBLIC);
                getter.returnValue(getter.readInstanceField(fieldCreator.getFieldDescriptor(), getter.getThis()));
            }
        }

        return new GeneratedBeanBuildItem(classOutput.getGeneratedClass("com.example.runtime.CollectableClassService"), true);
    }*/
}
