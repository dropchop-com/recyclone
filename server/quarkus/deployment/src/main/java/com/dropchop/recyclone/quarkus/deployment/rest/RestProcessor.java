package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.rest.OasFilter;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import com.dropchop.recyclone.quarkus.runtime.spi.bean.RecycloneApplicationImpl;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BuildTimeConditionBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.resteasy.reactive.spi.AdditionalResourceClassBuildItem;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.smallrye.openapi.jaxrs.JaxRsConstants;
import io.smallrye.openapi.runtime.util.JandexUtil;
import io.smallrye.openapi.spring.SpringConstants;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

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

  private static final DotName POST_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.POST"
  );

  private static final DotName PUT_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.PUT"
  );

  private static final DotName DELETE_ANNOTATION = DotName.createSimple(
      "jakarta.ws.rs.DELETE"
  );

  private static final DotName JUTIL_LIST = DotName.createSimple(
      "java.util.List"
  );

  private static final DotName JUTIL_COLLECTION = DotName.createSimple(
      "java.util.Collection"
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

  private static String extractSecondFromLastPathSegment(String pathValue) {
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

  private static boolean shouldExclude(ClassInfo apiClass, RecycloneBuildConfig config) {
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

  private static boolean isOrSubtype(DotName is, ClassInfo classInfo, IndexView index) {
    // Check if the current class is java.util.Collection
    if (classInfo.name().equals(is)) {
      return true;
    }

    // Check implemented interfaces
    for (DotName interfaceName : classInfo.interfaceNames()) {
      ClassInfo interfaceInfo = index.getClassByName(interfaceName);
      if (interfaceInfo != null && isOrSubtype(is, interfaceInfo, index)) {
        return true;
      }
    }

    // Check superclass
    DotName superClassName = classInfo.superName();
    if (superClassName != null && !superClassName.equals(DotName.createSimple("java.lang.Object"))) {
      ClassInfo superClassInfo = index.getClassByName(superClassName);
      return superClassInfo != null && isOrSubtype(is, superClassInfo, index);
    }

    return false;
  }

  private static ClassInfo findLastImplementor(IndexView indexView, ClassInfo apiClass) {
    Collection<ClassInfo> classes = indexView.getAllKnownImplementors(apiClass.name());
    // Identify leaf classes
    ClassInfo implClass = null;
    for (ClassInfo implementor : classes) {
      Collection<ClassInfo> subClasses = indexView.getAllKnownSubclasses(implementor.name());
      if (subClasses.isEmpty()) {
        implClass = implementor;
      }
    }
    return implClass;
  }

  @BuildStep
  public void buildRestMapping(CombinedIndexBuildItem cibi,
                               RecycloneBuildConfig config,
                               BuildProducer<RestMappingBuildItem> restMappingItemBuildProducer) {
    List<AnnotationInstance> openapiAnnotations = new ArrayList<>();
    Set<DotName> allOpenAPIEndpoints = getAllOpenAPIEndpoints();

    IndexView indexView = cibi.getIndex();
    for (DotName dotName : allOpenAPIEndpoints) {
      openapiAnnotations.addAll(indexView.getAnnotations(dotName));
    }

    RestMapping mapping = new RestMapping(
        ConfigUtils.isProfileActive("dev") || ConfigUtils.isProfileActive("test")
    );

    for (AnnotationInstance ai : openapiAnnotations) {
      if (!ai.target().kind().equals(AnnotationTarget.Kind.METHOD)) {
        continue;
      }
      MethodInfo method = ai.target().asMethod();
      ClassInfo apiClass = method.declaringClass();
      if (!apiClass.hasAnnotation(DYN_CTX_ANNO)) {
        continue;
      }

      //in recyclone all meta-data is annotated on an interface except the actual @Path
      if (!(Modifier.isInterface(apiClass.flags()) || Modifier.isAbstract(apiClass.flags()))) {
        log.warn("Contract violation: The [{}] annotation should be declared on an interface or an abstract class!");
        continue;
      }

      // Check for @Path annotation
      AnnotationInstance pathAnnotation = apiClass.declaredAnnotation(PATH_ANNOTATION);
      if (pathAnnotation == null) {
        continue;
      }
      String path = pathAnnotation.value().asString();
      pathAnnotation = method.declaredAnnotation(PATH_ANNOTATION);
      String tmp;
      if (pathAnnotation == null) {
        tmp = "";
      } else {
        tmp = pathAnnotation.value().asString();
        if (tmp == null) {
          tmp = "";
        }
        if (!tmp.isBlank() && !tmp.startsWith("/") && !path.endsWith("/")) {
          tmp = "/" + tmp;
        }
      }
      String methodSegment = tmp;
      String methodPath = path + methodSegment;

      AnnotationInstance dynamicExecAnnotation = apiClass.annotation(DYN_CTX_ANNO);
      // Extract and check values from @DynamicExecContext
      DotName classParamClass = getClassAnnotationValue(
          dynamicExecAnnotation, "value", ANNO_VALUE
      );
      DotName tmpDataClass = getClassAnnotationValue(
          dynamicExecAnnotation, "dataClass", ANNO_DATA_CLASS
      );
      DotName methodParamClass = null;
      if (!method.hasAnnotation(GET_ANNOTATION)) {
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
      String segment = extractSecondFromLastPathSegment(path);

      DotName execContextClass = getClassAnnotationValue(
          dynamicExecAnnotation, "execContextClass", ANNO_EXEC_CTX_CLASS
      );
      boolean internal = getBooleanAnnotationValue(dynamicExecAnnotation, "internal", ANNO_INTERNAL);

      Collection<String> implementors = indexView.getAllKnownImplementors(apiClass.name())
              .stream().map(c -> c.name().toString()).collect(Collectors.toSet());

      // Identify leaf class
      ClassInfo implClass = findLastImplementor(indexView, apiClass);

      boolean doExclude = shouldExclude(apiClass, config);
      //Get correct REST resource path
      String rewrittenPath;
      boolean implMissingPath;
      if (implClass != null) {
        if (!implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) {
          rewrittenPath = internal ? "/internal" + path : "/public" + path;
          implMissingPath = true;
        } else {
          rewrittenPath = apiClass.declaredAnnotation(PATH_ANNOTATION).value().asString();
          implMissingPath = false;
        }
      } else {
        rewrittenPath = path;
        implMissingPath = true;
      }
      String rewrtittenMethodPath = rewrittenPath + methodSegment;

      RestClass restClass = mapping.addApiClass(apiClass.name().toString(),
          () -> new RestClass(
              apiClass.name().toString(),
              implClass != null ? implClass.name().toString() : null,
              implementors,
              execContextClass != null ? execContextClass.toString() : null,
              classParamClass != null ? classParamClass.toString() : null,
              path,
              rewrittenPath,
              internal,
              implMissingPath
          )
      );

      RestMethod.Action action;
      String verb;
      if (method.hasAnnotation(GET_ANNOTATION)) {
        action = RestMethod.Action.READ;
        verb = GET_ANNOTATION.withoutPackagePrefix();
      } else if (method.hasAnnotation(PUT_ANNOTATION)) {
        action = RestMethod.Action.UPDATE;
        verb = PUT_ANNOTATION.withoutPackagePrefix();
      } else if (method.hasAnnotation(POST_ANNOTATION)) {
        verb = POST_ANNOTATION.withoutPackagePrefix();
        if (methodParamClass == null) {
          action = RestMethod.Action.READ;
        } else if (dataClass == null) {
          action = RestMethod.Action.READ;
        } else {
          ClassInfo methodParamClassInfo = indexView.getClassByName(methodParamClass);
          ClassInfo dataClassInfo = indexView.getClassByName(dataClass);
          if (methodParamClassInfo == null
              && methodParamClass.equals(JUTIL_LIST)
              && isOrSubtype(ANNO_DATA_CLASS, dataClassInfo, indexView)) {
            action = RestMethod.Action.CREATE;
          } else if (methodParamClassInfo != null
              && isOrSubtype(JUTIL_COLLECTION, methodParamClassInfo, indexView)
              && isOrSubtype(ANNO_DATA_CLASS, dataClassInfo, indexView)) {
            action = RestMethod.Action.CREATE;
          } else {
            action = RestMethod.Action.READ;
          }
        }
      } else if (method.hasAnnotation(DELETE_ANNOTATION)) {
        action = RestMethod.Action.DELETE;
        verb = DELETE_ANNOTATION.withoutPackagePrefix();
      } else {
        action = RestMethod.Action.OTHER;
        verb = "UNKNOWN";
      }

      String methodRef = JandexUtil.createUniqueMethodReference(apiClass, method);
      String implMethodRef;
      if (implClass != null) { // Add implementation method
        Type[] params = method.parameterTypes().toArray(new Type[] {});
        MethodInfo implMethod = implClass.method(method.name(), params);
        if (implMethod != null) { // some interfaces can have default methods
          implMethodRef = JandexUtil.createUniqueMethodReference(implClass, implMethod);
        } else {
          implMethodRef = null;
        }
      } else {
        implMethodRef = null;
      }

      RestMethod restMethodMapping = new RestMethod(
          restClass,
          methodRef,
          implMethodRef,
          methodParamClass != null ? methodParamClass.toString() : null,
          dataClass != null ? dataClass.toString() : null,
          action,
          method.name(),
          verb,
          internal,
          methodPath,
          rewrtittenMethodPath,
          segment,
          doExclude
      );

      restClass.apiMethodRef(methodRef, restMethodMapping);
      if (implMethodRef != null) {
        restClass.implMethodRef(implMethodRef, restMethodMapping);
        mapping.addImplMethod(implMethodRef, restMethodMapping);
      }
      mapping.addApiMethod(methodRef, restMethodMapping);
    }

    restMappingItemBuildProducer.produce(
        new RestMappingBuildItem(mapping)
    );
  }

  @BuildStep
  public void addMissingPathImplementationRestResources(
      CombinedIndexBuildItem cibi,
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
      BuildProducer<AdditionalResourceClassBuildItem> additionalProducer) {
    IndexView indexView = cibi.getIndex();
    for (RestClass mapping : restMappingBuildItem.getMapping().getApiClasses()) {
      if (mapping.isExcluded()) {
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.getApiClass()
      ).build());
      String implClass = mapping.getImplClass();
      if (implClass != null && mapping.isImplMissingPath()) {
        ClassInfo implClassInfo = indexView.getClassByName(implClass);
        reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
            implClass
        ).build());
        additionalProducer.produce(
            new AdditionalResourceClassBuildItem(implClassInfo, mapping.getRewrittenPath())
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
  }

  @BuildStep
  public void processExclusions(
      CombinedIndexBuildItem cibi,
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<BuildTimeConditionBuildItem> conditionBuildItemsProducer) {
    IndexView indexView = cibi.getIndex();
    for (RestClass mapping : restMappingBuildItem.getMapping().getApiClasses()) {
      if (mapping.isExcluded()) {
        ClassInfo apiClass = indexView.getClassByName(mapping.getApiClass());
        if (apiClass.hasDeclaredAnnotation(PATH_ANNOTATION)) {
          addExclude(conditionBuildItemsProducer, PATH_ANNOTATION, apiClass);
        }
        for (String implClass : mapping.getImplementors()) {
          ClassInfo implClassInfo = indexView.getClassByName(implClass);
          if (implClassInfo.hasDeclaredAnnotation(PATH_ANNOTATION)) {
            addExclude(conditionBuildItemsProducer, PATH_ANNOTATION, implClassInfo);
          } else if (implClassInfo.hasDeclaredAnnotation(REQ_SCOPED_ANNOTATION)) {
            addExclude(conditionBuildItemsProducer, REQ_SCOPED_ANNOTATION, implClassInfo);
          } else if (implClassInfo.hasDeclaredAnnotation(APP_SCOPED_ANNOTATION)) {
            addExclude(conditionBuildItemsProducer, APP_SCOPED_ANNOTATION, implClassInfo);
          }
        }
      }
    }
  }

  @BuildStep
  void filterOpenAPI(RestMappingBuildItem restMappingBuildItem,
                     RecycloneBuildConfig recycloneBuildConfig,
                     BuildProducer<AddToOpenAPIDefinitionBuildItem> addToOpenAPIDefinitionBuildItemBuildProducer) {
    addToOpenAPIDefinitionBuildItemBuildProducer.produce(new AddToOpenAPIDefinitionBuildItem(
        new OasFilter(recycloneBuildConfig, restMappingBuildItem.getMapping())
    ));
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
