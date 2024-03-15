package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.rest.RestRecorder;
import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneApplication;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.smallrye.openapi.deployment.OpenApiFilteredIndexViewBuildItem;
import io.smallrye.openapi.jaxrs.JaxRsConstants;
import io.smallrye.openapi.runtime.scanner.FilteredIndexView;
import io.smallrye.openapi.spring.SpringConstants;
import jakarta.inject.Singleton;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.lang.reflect.Modifier;
import java.util.*;

import static io.quarkus.deployment.annotations.ExecutionTime.RUNTIME_INIT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
public class RestProcessor {

  private static final Logger log = Logger.getLogger("com.dropchop.recyclone.quarkus");

  private final DotName DYN_CTX_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.rest.jaxrs.api.DynamicExecContext"
  );

  private static final DotName PATH_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.Path"
  );

  private static final DotName GET_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.GET"
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

  /*@BuildStep
  OpenApiFilteredIndexViewBuildItem smallryeOpenApiIndex(CombinedIndexBuildItem combinedIndexBuildItem,
                                                         BeanArchiveIndexBuildItem beanArchiveIndexBuildItem,
                                                         BuildExclusionsBuildItem buildExclusionsBuildItem) {

    CompositeIndex compositeIndex = CompositeIndex.create(
        combinedIndexBuildItem.getIndex(),
        beanArchiveIndexBuildItem.getIndex());

    OpenApiConfig config = OpenApiConfig.fromConfig(ConfigProvider.getConfig());
    Set<DotName> buildTimeClassExclusions = buildExclusionsBuildItem.getExcludedDeclaringClasses()
        .stream()
        .map(DotName::createSimple)
        .collect(Collectors.toSet());

    FilteredIndexView indexView = new FilteredIndexView(compositeIndex, config) {
      @Override
      public boolean accepts(DotName className) {
        if (super.accepts(className)) {
          return !buildTimeClassExclusions.contains(className);
        }

        return false;
      }
    };

    return new OpenApiFilteredIndexViewBuildItem(indexView);
  }*/

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

  private static Set<DotName> getAllOpenAPIEndpoints() {
    Set<DotName> httpAnnotations = new HashSet<>();
    httpAnnotations.addAll(JaxRsConstants.HTTP_METHODS);
    httpAnnotations.addAll(SpringConstants.HTTP_METHODS);
    return httpAnnotations;
  }

  private String extractSecondFromLastPathSegment(String pathValue) {
    // Assuming path starts with "/", remove it for splitting
    String normalizedPath = pathValue.startsWith("/") ? pathValue.substring(1) : pathValue;
    String[] segments = normalizedPath.split("/");

    // Return the second segment or null if not available
    if (segments.length >= 2) {
      return segments[segments.length - 2];
    } else if (segments.length == 1) {
      return segments[segments.length - 1];
    }
    return null;
  }

  @BuildStep
  public void buildRestMapping(
                               OpenApiFilteredIndexViewBuildItem filteredIndexView,
                               BuildProducer<RestMappingItem> restMappingItemBuildProducer) {
    FilteredIndexView filteredIndex = filteredIndexView.getIndex();
    List<AnnotationInstance> openapiAnnotations = new ArrayList<>();
    Set<DotName> allOpenAPIEndpoints = getAllOpenAPIEndpoints();
    for (DotName dotName : allOpenAPIEndpoints) {
      openapiAnnotations.addAll(filteredIndex.getAnnotations(dotName));
    }

    Map<MethodInfo, RestMapping> mapping = new HashMap<>();
    for (AnnotationInstance ai : openapiAnnotations) {
      if (!ai.target().kind().equals(AnnotationTarget.Kind.METHOD)) {
        continue;
      }
      MethodInfo method = ai.target().asMethod();
      ClassInfo declaringClass = method.declaringClass();
      if (!declaringClass.hasAnnotation(DYN_CTX_ANNO)) {
        continue;
      }

      //in recyclone all meta-data is annotated on an interface except the actual @Path
      if (!(Modifier.isInterface(declaringClass.flags()) || Modifier.isAbstract(declaringClass.flags()))) {
        log.warn("Contract violation: The [{}] annotation should be declared on an interface or an abstract class!");
        continue;
      }

      // Check for @Path annotation
      AnnotationInstance pathAnnotation = declaringClass.declaredAnnotation(PATH_ANNOTATION);
      if (pathAnnotation == null) {
        continue;
      }

      AnnotationInstance dynamicExecAnnotation = declaringClass.annotation(DYN_CTX_ANNO);
      // Extract and check values from @DynamicExecContext
      DotName classParamClass = getClassAnnotationValue(
          dynamicExecAnnotation, "value", ANNO_VALUE
      );
      DotName dataClass = getClassAnnotationValue(
          dynamicExecAnnotation, "dataClass", ANNO_DATA_CLASS
      );
      DotName methodParamClass = null;
      if (!method.hasAnnotation(GET_ANNOTATION)) {
        classParamClass = null;
        List<Type> types = method.parameterTypes();
        if (types.size() != 1) {
          log.warn("Contract violation: The rest method should have only one parameter class [{}] ");
        } else {
          Type methodParameterType = types.iterator().next();
          methodParamClass = methodParameterType.name();
          if (methodParameterType.kind() == Type.Kind.PARAMETERIZED_TYPE) {
            if (!methodParameterType.asParameterizedType().arguments().isEmpty()) {
              // Get the first type argument
              Type typeArgument = methodParameterType.asParameterizedType().arguments().get(0);
              dataClass = typeArgument.name();
            }
          }
        }
      }

      String path = pathAnnotation.value().asString();
      String segment = extractSecondFromLastPathSegment(path);

      DotName execContextClass = getClassAnnotationValue(
          dynamicExecAnnotation, "execContextClass", ANNO_EXEC_CTX_CLASS
      );
      boolean internal = getBooleanAnnotationValue(dynamicExecAnnotation, "internal", ANNO_INTERNAL);

      mapping.put(method, new RestMapping(
          declaringClass,
          method,
          classParamClass,
          methodParamClass,
          dataClass,
          execContextClass,
          internal,
          path,
          segment,
          false
      ));

    }
    restMappingItemBuildProducer.produce(new RestMappingItem(mapping));
  }

  @BuildStep
  void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(RecycloneApplication.class)
            .setUnremovable()
            .setDefaultScope(DotNames.SINGLETON).build()
    );
  }

  @BuildStep
  @Record(RUNTIME_INIT)
  public void registerResourceClasses(RestMappingItem restMappingItem,
                                      OpenApiFilteredIndexViewBuildItem filteredIndexView,
                                      RestRecorder recorder) {
    FilteredIndexView filteredIndex = filteredIndexView.getIndex();
    Collection<String> resultClasses = new LinkedHashSet<>();
    for (Map.Entry<MethodInfo, RestMapping> entry : restMappingItem.getMapping().entrySet()) {
      RestMapping mapping = entry.getValue();
      Collection<ClassInfo> classes = filteredIndex.getAllKnownImplementors(mapping.apiClass.name());
      for (ClassInfo impl : classes) {
        resultClasses.add(impl.name().toString());
      }
    }

    recorder.createRestApplication(resultClasses);
  }
}
