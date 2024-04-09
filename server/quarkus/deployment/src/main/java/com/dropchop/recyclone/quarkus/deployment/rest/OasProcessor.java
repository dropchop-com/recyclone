package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.rest.OasFilter;
import com.dropchop.recyclone.quarkus.runtime.rest.OasMapping;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.smallrye.openapi.runtime.util.JandexUtil;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.util.*;

/**
 * JAX-RS detection taken from:
 * extensions/smallrye-openapi/deployment/src/main/java/io/quarkus/smallrye/openapi/deployment/SmallRyeOpenApiProcessor.java
 *
 * @author Guillaume Smet <guillaume.smet@gmail.com>
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 24.
 */
@SuppressWarnings("unused")
public class OasProcessor {

  private static final Logger log = Logger.getLogger("com.dropchop.recyclone.quarkus");

  private static final DotName GET_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.GET"
  );

  private static final DotName PATH_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.Path"
  );

  private void constructMappings(CombinedIndexBuildItem indexBuildItem, RestMappingBuildItem restMappingBuildItem,
                                 Map<String, OasMapping> operationMapping, Map<String, String> pathMapping,
                                 boolean hideInternal) {
    IndexView indexView = indexBuildItem.getIndex();
    for (Map.Entry<MethodInfo, RestMethodMapping> entry : restMappingBuildItem.getMethodMapping().entrySet()) {
      RestMethodMapping mapping = entry.getValue();

      if (mapping.internal) {
        pathMapping.put(mapping.path, "/internal" + mapping.path);
        if (hideInternal) {
          continue;
        }
      } else {
        pathMapping.put(mapping.path, "/public" + mapping.path);
      }

      MethodInfo method = mapping.apiMethod;
      ClassInfo declaringClass = mapping.apiClass;
      Type[] params = method.parameterTypes().toArray(new Type[] {});

      Set<String> tags = new HashSet<>();
      if (mapping.segment != null) {
        tags.add(mapping.segment);
      }

      // Extract and check values from @DynamicExecContext
      DotName valueClassName = mapping.classParamClass;
      // only GET methods can have dynamic params with class annotated parameter class name
      if (!method.hasAnnotation(GET_ANNOTATION)) {
        valueClassName = null;
      }

      Collection<ClassInfo> classes = indexView.getAllKnownImplementors(declaringClass.name());
      boolean implMissingPathAnnotation = false;
      for (ClassInfo impl : classes) {
        MethodInfo implMethod = impl.method(method.name(), params);

        if (implMethod != null) {// we keep implementations so we get correct path
          OasMapping config = new OasMapping(
              JandexUtil.createUniqueMethodReference(impl, implMethod),
              impl.name().toString(),
              implMethod.name(),
              valueClassName != null ? valueClassName.toString() : null,
              tags,
              mapping.excluded
          );
          operationMapping.put(config.getMethodRef(), config);
        }

        OasMapping config = new OasMapping(
            JandexUtil.createUniqueMethodReference(impl, method),
            impl.name().toString(),
            method.name(),
            valueClassName != null ? valueClassName.toString() : null,
            tags,
            mapping.excluded
        );
        operationMapping.put(config.getMethodRef(), config);

        if (!impl.hasDeclaredAnnotation(PATH_ANNOTATION)) {
          implMissingPathAnnotation = true;
        }
      }

      //filter out interface methods to avoid duplication but only exclude if impl has path anno
      OasMapping config = new OasMapping(
          JandexUtil.createUniqueMethodReference(declaringClass, method),
          declaringClass.name().toString(),
          method.name(),
          valueClassName != null ? valueClassName.toString() : null,
          tags,
          !implMissingPathAnnotation
      );
      operationMapping.put(config.getMethodRef(), config);
    }
  }

  @BuildStep
  void filterOpenAPI(CombinedIndexBuildItem indexBuildItem,
                     RestMappingBuildItem restMappingBuildItem,
                     RecycloneBuildConfig recycloneBuildConfig,
                     BuildProducer<AddToOpenAPIDefinitionBuildItem> addToOpenAPIDefinitionBuildItemBuildProducer) {
    Map<String, OasMapping> operationMapping = new HashMap<>();
    Map<String, String> pathMapping = new HashMap<>();
    if (ConfigUtils.isProfileActive("dev") || ConfigUtils.isProfileActive("test")) {
      constructMappings(indexBuildItem, restMappingBuildItem, operationMapping, pathMapping,false);
    } else {
      constructMappings(indexBuildItem, restMappingBuildItem, operationMapping, pathMapping,true);
    }

    addToOpenAPIDefinitionBuildItemBuildProducer.produce(new AddToOpenAPIDefinitionBuildItem(
        new OasFilter(recycloneBuildConfig, operationMapping, pathMapping)
    ));
  }
}
