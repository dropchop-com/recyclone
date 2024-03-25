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

  private Map<String, OasMapping> constructMappings(CombinedIndexBuildItem indexBuildItem,
                                                    RestMappingBuildItem restMappingBuildItem,
                                                    boolean hideInternal) {
    IndexView indexView = indexBuildItem.getIndex();
    Map<String, OasMapping> operationFilter = new HashMap<>();

    for (Map.Entry<MethodInfo, RestMethodMapping> entry : restMappingBuildItem.getMethodMapping().entrySet()) {
      RestMethodMapping mapping = entry.getValue();
      if (mapping.internal && hideInternal) {
        continue;
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
      for (ClassInfo impl : classes) {
        MethodInfo implMethod = impl.method(method.name(), params);

        if (implMethod != null) {// we register implementations so we get correct path
          OasMapping config = new OasMapping(
              JandexUtil.createUniqueMethodReference(impl, implMethod),
              impl.name().toString(),
              implMethod.name(),
              valueClassName != null ? valueClassName.toString() : null,
              tags,
              mapping.excluded
          );
          operationFilter.put(config.getMethodRef(), config);
        }

        OasMapping config = new OasMapping(
            JandexUtil.createUniqueMethodReference(impl, method),
            impl.name().toString(),
            method.name(),
            valueClassName != null ? valueClassName.toString() : null,
            tags,
            mapping.excluded
        );
        operationFilter.put(config.getMethodRef(), config);
      }

      //filter out interface methods to avoid duplication
      OasMapping config = new OasMapping(
          JandexUtil.createUniqueMethodReference(declaringClass, method),
          declaringClass.name().toString(),
          method.name()
          //valueClassName != null ? valueClassName.toString() : null,
          //tags
      );
      operationFilter.put(config.getMethodRef(), config);
    }
    return  operationFilter;
  }

  @BuildStep
  void filterOpenAPI(CombinedIndexBuildItem indexBuildItem,
                     RestMappingBuildItem restMappingBuildItem,
                     RecycloneBuildConfig recycloneBuildConfig,
                     BuildProducer<AddToOpenAPIDefinitionBuildItem> addToOpenAPIDefinitionBuildItemBuildProducer) {
    Map<String, OasMapping> operationMapping;
    if (ConfigUtils.isProfileActive("dev") || ConfigUtils.isProfileActive("test")) {
      operationMapping = constructMappings(indexBuildItem, restMappingBuildItem, false);
    } else {
      operationMapping = constructMappings(indexBuildItem, restMappingBuildItem, true);
    }
    addToOpenAPIDefinitionBuildItemBuildProducer.produce(new AddToOpenAPIDefinitionBuildItem(
        new OasFilter(recycloneBuildConfig, operationMapping)
    ));
  }
}
