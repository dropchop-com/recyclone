package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.recyclone.base.api.model.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.registry.RegistryRecorder;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.jandex.*;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

public class RegistryProcessor {

  private final DotName MAPPINGS_ANNO = DotName.createSimple(
      "org.mapstruct.SubclassMappings"
  );

  private final DotName MAPPING_ANNO = DotName.createSimple(
      "org.mapstruct.SubclassMapping"
  );

  private final DotName TYPE_INFO_ANNO = DotName.createSimple(
      "com.fasterxml.jackson.annotation.JsonTypeInfo"
  );

  private final DotName ENTITY_INTERFACE = DotName.createSimple(
      "com.dropchop.recyclone.base.api.model.base.Entity"
  );

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
  void collectEntityRootInterfaceClasses(CombinedIndexBuildItem combinedIndexBuildItem,
                                         BuildProducer<EntityRootInterfacesTypeItem> classProducer) {
    IndexView index = combinedIndexBuildItem.getIndex();
    Collection<String> entityInterfaces = new LinkedHashSet<>();
    for (ClassInfo entityRootMarker : index.getKnownDirectSubinterfaces(ENTITY_INTERFACE)) {
      entityInterfaces.add(entityRootMarker.name().toString());
    }
    classProducer.produce(new EntityRootInterfacesTypeItem(entityInterfaces));
  }

  @BuildStep
  void collectSubclassMappingAnnotatedClasses(CombinedIndexBuildItem combinedIndexBuildItem,
                                         BuildProducer<MapperSubTypeItem> classProducer) {
    IndexView index = combinedIndexBuildItem.getIndex();
    Map<String, String> mappingClasses = new LinkedHashMap<>();
    for (AnnotationInstance annotation : index.getAnnotations(MAPPING_ANNO)) {
      processMappingAnnotation(annotation, mappingClasses);
    }
    for (AnnotationInstance annotation : index.getAnnotations(MAPPINGS_ANNO)) {
      AnnotationInstance[] subclassMappingAnnotations = annotation.value().asNestedArray();
      if (subclassMappingAnnotations != null) {
        for (AnnotationInstance subclassMappingAnnotation : subclassMappingAnnotations) {
          processMappingAnnotation(subclassMappingAnnotation, mappingClasses);
        }
      }
    }

    classProducer.produce(new MapperSubTypeItem(mappingClasses));
  }

  @BuildStep
  void collectSerializationAnnotatedClasses(CombinedIndexBuildItem combinedIndexBuildItem,
                                            BuildProducer<JsonSerializationTypeItem> classProducer) {
    IndexView index = combinedIndexBuildItem.getIndex();
    Collection<String> propertyMappedClasses = new LinkedHashSet<>();
    for (AnnotationInstance annotation : index.getAnnotations(TYPE_INFO_ANNO)) {
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
            propertyMappedClasses.add(subClassInfo.name().toString());
          }
        }
      }
    }
    classProducer.produce(
        new JsonSerializationTypeItem(propertyMappedClasses)
    );
  }

  @BuildStep
  @Record(STATIC_INIT)
  SyntheticBeanBuildItem setupJsonSerializationTypeConfig(RegistryRecorder recorder,
                                                          JsonSerializationTypeItem serializationBuildItem) {
    return SyntheticBeanBuildItem.configure(JsonSerializationTypeConfig.class)
        .scope(ApplicationScoped.class)
        .unremovable()
        .runtimeValue(recorder.createJsonSerializationTypeConfig(serializationBuildItem.getClassNames()))
        .done();
  }

  @BuildStep
  @Record(STATIC_INIT)
  SyntheticBeanBuildItem setupMapperSubTypeConfig(RegistryRecorder recorder,
                                                  MapperSubTypeItem mappingBuildItems,
                                                  EntityRootInterfacesTypeItem entityRootInterfacesTypeItem) {
    return SyntheticBeanBuildItem.configure(MapperSubTypeConfig.class)
        .scope(ApplicationScoped.class)
        .unremovable()
        .runtimeValue(
            recorder.createMapperSubTypeConfig(
                mappingBuildItems.getClassNames(),
                entityRootInterfacesTypeItem.getClassNames()
            )
        )
        .done();
  }

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
      <FieldCreator fieldCreator = classCreator.getFieldCreator("classnames", Set.class.getName())
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
