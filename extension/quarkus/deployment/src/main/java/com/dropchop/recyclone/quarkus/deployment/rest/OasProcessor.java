package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.rest.OasMappingConfig;
import com.dropchop.recyclone.quarkus.runtime.rest.OasFilter;
import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneBuildConfig;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.smallrye.openapi.deployment.OpenApiFilteredIndexViewBuildItem;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.smallrye.openapi.runtime.scanner.FilteredIndexView;
import io.smallrye.openapi.runtime.util.JandexUtil;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;
import org.jboss.jandex.Type;
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

  private Map<String, OasMappingConfig> constructMappings(OpenApiFilteredIndexViewBuildItem filteredIndexView,
                                                          RestMappingItem restMappingItem,
                                                          boolean hideInternal) {
    FilteredIndexView filteredIndex = filteredIndexView.getIndex();
    Map<String, OasMappingConfig> operationFilter = new HashMap<>();

    for (Map.Entry<MethodInfo, RestMapping> entry : restMappingItem.getMapping().entrySet()) {
      RestMapping mapping = entry.getValue();
      if (mapping.internal && hideInternal) {
        continue;
      }

      MethodInfo method = mapping.apiMethod;
      ClassInfo declaringClass = mapping.apiClass;
      Type[] params = method.parameterTypes().toArray(new Type[] {});

      if (mapping.excluded) {
        //filter out excluded
        OasMappingConfig config = new OasMappingConfig(
            JandexUtil.createUniqueMethodReference(declaringClass, method),
            declaringClass.name().toString(),
            method.name()
        );
      }

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

      Collection<ClassInfo> classes = filteredIndex.getAllKnownImplementors(declaringClass.name());
      for (ClassInfo impl : classes) {
        MethodInfo implMethod = impl.method(method.name(), params);

        if (implMethod != null) {// we register implementations so we get correct path
          OasMappingConfig config = new OasMappingConfig(
              JandexUtil.createUniqueMethodReference(impl, implMethod),
              impl.name().toString(),
              implMethod.name(),
              valueClassName != null ? valueClassName.toString() : null,
              tags
          );
          operationFilter.put(config.getMethodRef(), config);
        }

        OasMappingConfig config = new OasMappingConfig(
            JandexUtil.createUniqueMethodReference(impl, method),
            impl.name().toString(),
            method.name(),
            valueClassName != null ? valueClassName.toString() : null,
            tags
        );
        operationFilter.put(config.getMethodRef(), config);
      }

      //filter out interface methods to avoid duplication
      OasMappingConfig config = new OasMappingConfig(
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
  void filterOpenAPI(OpenApiFilteredIndexViewBuildItem filteredIndexView,
                     RestMappingItem restMappingItem,
                     RecycloneBuildConfig recycloneBuildConfig,
                     BuildProducer<AddToOpenAPIDefinitionBuildItem> addToOpenAPIDefinitionBuildItemBuildProducer) {
    Map<String, OasMappingConfig> operationMapping;
    if (ConfigUtils.isProfileActive("dev") || ConfigUtils.isProfileActive("test")) {
      operationMapping = constructMappings(filteredIndexView, restMappingItem, false);
    } else {
      operationMapping = constructMappings(filteredIndexView, restMappingItem, true);
    }
    addToOpenAPIDefinitionBuildItemBuildProducer.produce(new AddToOpenAPIDefinitionBuildItem(
        new OasFilter(recycloneBuildConfig, operationMapping)
    ));
  }
}
