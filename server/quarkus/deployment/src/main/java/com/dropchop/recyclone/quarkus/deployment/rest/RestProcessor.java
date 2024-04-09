package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.spi.bean.RecycloneApplicationImpl;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.deployment.BuildTimeConditionBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.resteasy.reactive.spi.AdditionalResourceClassBuildItem;
import io.smallrye.openapi.jaxrs.JaxRsConstants;
import io.smallrye.openapi.spring.SpringConstants;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.lang.reflect.Modifier;
import java.util.*;

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

  private static final DotName REQ_SCOPED_ANNOTATION = DotName.createSimple(
  "jakarta.enterprise.context.RequestScoped"
  );

  private static final DotName APP_SCOPED_ANNOTATION = DotName.createSimple(
      "jakarta.enterprise.context.ApplicationScoped"
  );

  private static final boolean ANNO_INTERNAL = false;

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

  private boolean shouldExclude(Collection<ClassInfo> classes, RecycloneBuildConfig config) {
    RecycloneBuildConfig.Rest restConfig = config.rest;
    boolean doExclude = restConfig.includes.isPresent();
    for (ClassInfo impl : classes) {
      if (restConfig.includes.isPresent()) {
        for (String include : restConfig.includes.get()) {
          if (include.equals(impl.name().toString())) {
            doExclude = false;
            break;
          }
        }
      }
      if (restConfig.excludes.isPresent()) {
        for (String exclude : restConfig.excludes.get()) {
          if (exclude.equals(impl.name().toString())) {
            doExclude = true;
            break;
          }
        }
      }
    }

    return doExclude;
  }

  private boolean shouldExclude2(ClassInfo apiClass, RecycloneBuildConfig config) {
    RecycloneBuildConfig.Rest restConfig = config.rest;
    boolean doExclude = restConfig.includes.isPresent();
    if (restConfig.includes.isPresent()) {
      for (String include : restConfig.includes.get()) {
        if (include.equals(apiClass.name().toString())) {
          doExclude = false;
          break;
        }
      }
    }
    if (restConfig.excludes.isPresent()) {
      for (String exclude : restConfig.excludes.get()) {
        if (exclude.equals(apiClass.name().toString())) {
          doExclude = true;
          break;
        }
      }
    }

    return doExclude;
  }

  @BuildStep
  public void buildRestMapping(CombinedIndexBuildItem cibi,
                               RecycloneBuildConfig config,
                               BuildProducer<RestMappingBuildItem> restMappingItemBuildProducer) {
    List<AnnotationInstance> openapiAnnotations = new ArrayList<>();
    Set<DotName> allOpenAPIEndpoints = getAllOpenAPIEndpoints();
    for (DotName dotName : allOpenAPIEndpoints) {
      openapiAnnotations.addAll(cibi.getIndex().getAnnotations(dotName));
    }

    Map<MethodInfo, RestMethodMapping> methodMapping = new HashMap<>();
    Map<ClassInfo, RestClassMapping> classMapping = new HashMap<>();
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
      DotName tmpDataClass = getClassAnnotationValue(
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
              tmpDataClass = typeArgument.name();
            }
          }
        }
      }
      DotName dataClass = tmpDataClass;
      String path = pathAnnotation.value().asString();
      String segment = extractSecondFromLastPathSegment(path);

      DotName execContextClass = getClassAnnotationValue(
          dynamicExecAnnotation, "execContextClass", ANNO_EXEC_CTX_CLASS
      );
      boolean internal = getBooleanAnnotationValue(dynamicExecAnnotation, "internal", ANNO_INTERNAL);

      Collection<ClassInfo> classes = cibi.getIndex().getAllKnownImplementors(declaringClass.name());
      boolean doExclude = this.shouldExclude2(declaringClass, config);
      RestMethodMapping restMethodMapping = new RestMethodMapping(
          declaringClass,
          classes,
          method,
          classParamClass,
          methodParamClass,
          dataClass,
          execContextClass,
          internal,
          path,
          segment,
          doExclude
      );
      methodMapping.put(method, restMethodMapping);

      RestClassMapping restClassMapping = classMapping.computeIfAbsent(declaringClass, RestClassMapping::new);
      restClassMapping.methodMappings.add(restMethodMapping);
      restClassMapping.addImplementors(classes);
      restClassMapping.setPath(path);
      restClassMapping.setExcluded(doExclude);
      restClassMapping.setInternal(internal);
    }

    for (Map.Entry<ClassInfo, RestClassMapping> entry : classMapping.entrySet()) {
      RestClassMapping mapping = entry.getValue();
      mapping.setExcluded(
          mapping.methodMappings.stream().allMatch(RestMethodMapping::isExcluded)
      );
    }

    restMappingItemBuildProducer.produce(
        new RestMappingBuildItem(methodMapping, classMapping)
    );
  }

  @BuildStep
  public void processPathAnnotation(RestMappingBuildItem restMappingBuildItem,
                                    BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
                                    BuildProducer<AdditionalResourceClassBuildItem> additionalProducer) {
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.isExcluded()) {
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.apiClass.name().toString()
      ).build());
      for (ClassInfo implClass : mapping.implementors) {
        if (implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) { // keep defined/desired path annotation
          continue;
        }
        reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
            implClass.name().toString()
        ).build());
        String newPath = mapping.isInternal() ? "/internal" + mapping.getPath() : "/public" + mapping.getPath();
        additionalProducer.produce(
            new AdditionalResourceClassBuildItem(implClass, newPath)
        );
      }
    }
  }

  private void addExclude(BuildProducer<BuildTimeConditionBuildItem> conditionBuildItemsProducer,
                          DotName anno, ClassInfo classInfo) {
    AnnotationTarget t = classInfo.declaredAnnotation(anno).target();
    conditionBuildItemsProducer.produce(
        new BuildTimeConditionBuildItem(t, false)
    );
    log.debugv("Found REST resource excluded [{}]!", classInfo);
  }

  @BuildStep
  public void processExclusions(
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<BuildTimeConditionBuildItem> conditionBuildItemsProducer) {
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.isExcluded()) {
        if (mapping.apiClass.hasDeclaredAnnotation(PATH_ANNOTATION)) {
          addExclude(conditionBuildItemsProducer, PATH_ANNOTATION, mapping.apiClass);
        }
        for (ClassInfo implClass : mapping.implementors) {
          if (implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) {
            addExclude(conditionBuildItemsProducer, PATH_ANNOTATION, implClass);
          } else if (implClass.hasDeclaredAnnotation(REQ_SCOPED_ANNOTATION)) {
            addExclude(conditionBuildItemsProducer, REQ_SCOPED_ANNOTATION, implClass);
          } else if (implClass.hasDeclaredAnnotation(APP_SCOPED_ANNOTATION)) {
            addExclude(conditionBuildItemsProducer, APP_SCOPED_ANNOTATION, implClass);
          }
        }
      }
    }
  }

  @BuildStep
  public void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(RecycloneApplicationImpl.class)
            .setUnremovable()
            .setDefaultScope(DotNames.SINGLETON).build()
    );
  }
}
