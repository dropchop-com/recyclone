package com.dropchop.recyclone.extension.quarkus.deployment;

import com.dropchop.recyclone.extension.quarkus.swagger.MappingConfig;
import com.dropchop.recyclone.extension.quarkus.swagger.SwaggerUIFilter;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.smallrye.openapi.deployment.OpenApiFilteredIndexViewBuildItem;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.smallrye.openapi.jaxrs.JaxRsConstants;
import io.smallrye.openapi.runtime.scanner.FilteredIndexView;
import io.smallrye.openapi.runtime.util.JandexUtil;
import io.smallrye.openapi.spring.SpringConstants;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * JAX-RS detection taken from:
 * extensions/smallrye-openapi/deployment/src/main/java/io/quarkus/smallrye/openapi/deployment/SmallRyeOpenApiProcessor.java
 *
 * @author Guillaume Smet <guillaume.smet@gmail.com>
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 24.
 */
@SuppressWarnings("unused")
public class SwaggerUIProcessor {

  private static final Logger log = Logger.getLogger("com.dropchop.recyclone.extension.quarkus");

  private static final DotName PATH_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.Path"
  );

  private static final DotName GET_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.GET"
  );

  private final DotName DYN_CTX_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext"
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


  private static Set<DotName> getAllOpenAPIEndpoints() {
    Set<DotName> httpAnnotations = new HashSet<>();
    httpAnnotations.addAll(JaxRsConstants.HTTP_METHODS);
    httpAnnotations.addAll(SpringConstants.HTTP_METHODS);
    return httpAnnotations;
  }

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

  private Map<String, MappingConfig> constructMappings(OpenApiFilteredIndexViewBuildItem filteredIndexView,
                                                       boolean hideInternal) {
    FilteredIndexView filteredIndex = filteredIndexView.getIndex();
    List<AnnotationInstance> openapiAnnotations = new ArrayList<>();
    Set<DotName> allOpenAPIEndpoints = getAllOpenAPIEndpoints();
    for (DotName dotName : allOpenAPIEndpoints) {
      openapiAnnotations.addAll(filteredIndex.getAnnotations(dotName));
    }

    Map<String, MappingConfig> operationFilter = new HashMap<>();
    for (AnnotationInstance ai : openapiAnnotations) {
      if (ai.target().kind().equals(AnnotationTarget.Kind.METHOD)) {
        MethodInfo method = ai.target().asMethod();
        ClassInfo declaringClass = method.declaringClass();
        Type[] params = method.parameterTypes().toArray(new Type[] {});

        if (!declaringClass.hasAnnotation(DYN_CTX_ANNO)) {
          continue;
        }

        //in recyclone all meta-data is annotated on an interface except the actual @Path
        if (!(Modifier.isInterface(declaringClass.flags()) || Modifier.isAbstract(declaringClass.flags()))) {
          log.warn("Contract violation: The [{}] annotation should be declared on an interface or an abstract class!");
          continue;
        }

        AnnotationInstance dynamicExecAnnotation = declaringClass.annotation(DYN_CTX_ANNO);

        // Check for @Path annotation
        AnnotationInstance pathAnnotation = declaringClass.declaredAnnotation(PATH_ANNOTATION);
        if (pathAnnotation == null) {
          continue;
        }

        String pathSegment = extractSecondFromLastPathSegment(pathAnnotation);
        Set<String> tags = new HashSet<>();
        if (pathSegment != null) {
          tags.add(pathSegment);
        }

        // Extract and check values from @DynamicExecContext
        DotName valueClassName = getClassAnnotationValue(
            dynamicExecAnnotation, "value", ANNO_VALUE
        );
        // only GET methods can have dynamic params
        if (!method.hasAnnotation(GET_ANNOTATION)) {
          valueClassName = null;
        }
        //DotName dataClassName = getClassAnnotationValue(
        //    dynamicExecAnnotation, "dataClass", DTO
        //);
        //DotName execContextClassName = getClassAnnotationValue(
        //    dynamicExecAnnotation, "execContextClass", EXEC_CONTEXT
        //);
        //if (getBooleanAnnotationValue(dynamicExecAnnotation, "internal", ANNO_INTERNAL)) {
        //  tags.add("internal");
        //} else {
        //  tags.add("public");
        //}
        if (getBooleanAnnotationValue(dynamicExecAnnotation, "internal", ANNO_INTERNAL) && hideInternal) {
          continue;
        }
        Collection<ClassInfo> classes = filteredIndex.getAllKnownImplementors(declaringClass.name());
        for (ClassInfo impl : classes) {
          MethodInfo implMethod = impl.method(method.name(), params);

          if (implMethod != null) {// we register implementations so we get correct path
            MappingConfig config = new MappingConfig(
                JandexUtil.createUniqueMethodReference(impl, implMethod),
                impl.name().toString(),
                implMethod.name(),
                valueClassName != null ? valueClassName.toString() : null,
                tags
            );
            operationFilter.put(config.getMethodRef(), config);
          }

          MappingConfig config = new MappingConfig(
              JandexUtil.createUniqueMethodReference(impl, method),
              impl.name().toString(),
              method.name(),
              valueClassName != null ? valueClassName.toString() : null,
              tags
          );
          operationFilter.put(config.getMethodRef(), config);
        }

        //filter out interface methods to avoid duplication
        MappingConfig config = new MappingConfig(
            JandexUtil.createUniqueMethodReference(declaringClass, method),
            declaringClass.name().toString(),
            method.name()
            //valueClassName != null ? valueClassName.toString() : null,
            //tags
        );
        operationFilter.put(config.getMethodRef(), config);
      }

    }
    return operationFilter;
  }

  @BuildStep
  void filterOpenAPI(OpenApiFilteredIndexViewBuildItem combinedIndexBuildItem,
                   BuildProducer<AddToOpenAPIDefinitionBuildItem> addToOpenAPIDefinitionBuildItemBuildProducer) {
    Map<String, MappingConfig> operationMapping;
    if (ConfigUtils.isProfileActive("dev") || ConfigUtils.isProfileActive("test")) {
      operationMapping = constructMappings(combinedIndexBuildItem, false);
    } else {
      operationMapping = constructMappings(combinedIndexBuildItem, true);
    }
    addToOpenAPIDefinitionBuildItemBuildProducer.produce(new AddToOpenAPIDefinitionBuildItem(
        new SwaggerUIFilter(operationMapping)
    ));
  }
}
