package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.model.api.utils.Strings;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.rest.*;
import com.dropchop.recyclone.quarkus.runtime.rest.jaxrs.ContentTypeFilter;
import com.dropchop.recyclone.quarkus.runtime.rest.jaxrs.RestDynamicFeature;
import com.dropchop.recyclone.quarkus.runtime.rest.jaxrs.ServiceErrorExceptionMapper;
import com.dropchop.recyclone.quarkus.runtime.rest.openapi.OasFilter;
import com.dropchop.shiro.jaxrs.ShiroDynamicFeature;
import io.quarkus.arc.deployment.BuildTimeConditionBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.resteasy.reactive.spi.*;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.smallrye.openapi.deployment.spi.AddToOpenAPIDefinitionBuildItem;
import io.smallrye.openapi.jaxrs.JaxRsConstants;
import io.smallrye.openapi.runtime.util.JandexUtil;
import io.smallrye.openapi.spring.SpringConstants;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.jandex.*;
import org.jboss.logging.Logger;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static com.dropchop.recyclone.model.api.rest.Constants.Paths.INTERNAL_SEGMENT;
import static com.dropchop.recyclone.model.api.rest.Constants.Paths.PUBLIC_SEGMENT;
import static io.quarkus.deployment.annotations.ExecutionTime.STATIC_INIT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 03. 24.
 */
public class RestProcessor {

  private static final Logger log = Logger.getLogger("com.dropchop.recyclone.quarkus");

  private static final DotName DYN_CTX_ANNO = DotName.createSimple(
      "com.dropchop.recyclone.model.api.rest.DynamicExecContext"
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

  private static final DotName JAVA_UTIL_COLLECTION = DotName.createSimple(
      "java.util.Collection"
  );

  private static final DotName ANNO_VALUE = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.CommonParams"
  );

  private static final DotName ANNO_DATA_CLASS = DotName.createSimple(
      "com.dropchop.recyclone.model.api.base.Dto"
  );

  private static final DotName ANNO_EXEC_CTX_CLASS = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.CommonExecContext"
  );

  private static final DotName REQ_SCOPED_ANNOTATION = DotName.createSimple(
  "jakarta.enterprise.context.RequestScoped"
  );

  private static final DotName APP_SCOPED_ANNOTATION = DotName.createSimple(
      "jakarta.enterprise.context.ApplicationScoped"
  );

  private static final DotName CTX_PARAMS_IFACE = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.Params"
  );

  private static final boolean ANNO_INTERNAL = false;

  /**
   * Returns value but only if different from default
   */
  @SuppressWarnings("SameParameterValue")
  private static DotName getClassAnnotationValueIfDifferent(AnnotationInstance annotation, String property,
                                                            DotName defaultType) {
    AnnotationValue value = annotation.value(property);
    if (value == null) {
      return null;
    }
    DotName type = value.asClass().name();
    return !type.equals(defaultType) ? type : null;
  }

  @SuppressWarnings("SameParameterValue")
  private static boolean getBooleanAnnotationValue(AnnotationInstance annotation, String property,
                                                   boolean defaultType) {
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

  private static String computeMethodPathSegment(ClassInfo apiClass, MethodInfo methodInfo) {
    AnnotationInstance pathAnnotation = apiClass.declaredAnnotation(PATH_ANNOTATION);
    if (pathAnnotation == null) {
      return null;
    }
    String path = pathAnnotation.value().asString();
    pathAnnotation = methodInfo.declaredAnnotation(PATH_ANNOTATION);
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
    return tmp;
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

  /**
   * We support glob patterns with * and ? and reg ex if expr starts with ^
   */
  private static boolean matches(String expr, String target) {
    if (expr == null || expr.isBlank()) {
      return false;
    }
    if (expr.startsWith("^")) {
      return target.matches(expr);
    }
    if (expr.contains("*")) {
      if (expr.startsWith("\\")) {
        expr = expr.substring(1);
      }
      return Strings.match(expr, target);
    }
    return expr.equals(target);
  }

  /**
   * Compute config based inclusion / exclusion of rest interfaces.
   */
  private static boolean shouldExclude(ClassInfo apiClass, RecycloneBuildConfig config) {
    RecycloneBuildConfig.Rest restConfig = config.rest();
    boolean doExclude = restConfig.includes().isPresent() && !restConfig.includes().get().isEmpty();
    if (restConfig.includes().isPresent()) {
      for (String include : restConfig.includes().get()) {
        if (matches(include, apiClass.name().toString())) {
          doExclude = false;
          break;
        }
      }
    }
    if (restConfig.excludes().isPresent()) {
      for (String exclude : restConfig.excludes().get()) {
        if (matches(exclude, apiClass.name().toString())) {
          doExclude = true;
          break;
        }
      }
    }

    return doExclude;
  }

  /**
   * Check if a class is a type of or a subtype of a DotName
   */
  private static boolean isOrSubtype(DotName is, ClassInfo classInfo, IndexView index) {
    if (classInfo == null) {
      return false;
    }
    // Check if the current class is java.util.Collection
    if (classInfo.name().equals(is)) {
      return true;
    }

    // Check implemented interfaces
    for (DotName interfaceName : classInfo.interfaceNames()) {
      ClassInfo interfaceInfo = index.getClassByName(interfaceName);
      if (isOrSubtype(is, interfaceInfo, index)) {
        return true;
      }
    }

    // Check superclass
    DotName superClassName = classInfo.superName();
    if (superClassName != null && !superClassName.equals(DotName.createSimple("java.lang.Object"))) {
      ClassInfo superClassInfo = index.getClassByName(superClassName);
      return isOrSubtype(is, superClassInfo, index);
    }

    return false;
  }

  private static DotName[] computeParamDataClass(ClassInfo apiClass, MethodInfo method, IndexView indexView) {
    AnnotationInstance dynamicExecAnnotation = apiClass.annotation(DYN_CTX_ANNO);

    DotName tmpDataClass = getClassAnnotationValueIfDifferent(
        dynamicExecAnnotation, "dataClass", ANNO_DATA_CLASS
    );
    DotName methodParamClass = null;
    if (!method.hasAnnotation(GET_ANNOTATION)) {
      List<Type> types = method.parameterTypes();
      if (types.size() != 1) {
        log.warnf("Contract violation: The rest method should have only one parameter class [%s]", method);
      } else {
        Type methodParameterType = types.getFirst();
        methodParamClass = methodParameterType.name();
        if (methodParameterType.kind() == Type.Kind.PARAMETERIZED_TYPE) {
          if (!methodParameterType.asParameterizedType().arguments().isEmpty()) {
            // Get the first type argument
            Type typeArgument = methodParameterType.asParameterizedType().arguments().getFirst();
            if (typeArgument.kind() == Type.Kind.CLASS &&
                isOrSubtype(ANNO_DATA_CLASS,
                    indexView.getClassByName(typeArgument.asClassType().name()), indexView)) {
              tmpDataClass = typeArgument.name();
            }
          }
        }
      }
    }
    // if not set in annotation and not set by non-GET arguments look at return type
    if (tmpDataClass == null) {
      Type returnType = method.returnType();

      if (returnType.kind() == Type.Kind.CLASS) {
        if (isOrSubtype(ANNO_DATA_CLASS, indexView.getClassByName(returnType.asClassType().name()), indexView)) {
          tmpDataClass = returnType.asClassType().name();
        }
      } else if (returnType.kind() == Type.Kind.PARAMETERIZED_TYPE) {
        if (!returnType.asParameterizedType().arguments().isEmpty()) {
          // Get the first type argument
          Type typeArgument = returnType.asParameterizedType().arguments().getFirst();
          if (typeArgument.kind() == Type.Kind.CLASS &&
              isOrSubtype(ANNO_DATA_CLASS,
                  indexView.getClassByName(typeArgument.asClassType().name()), indexView)) {
            tmpDataClass = typeArgument.name();
          } else if (typeArgument.kind() == Type.Kind.TYPE_VARIABLE) {
            List<Type> bounds = typeArgument.asTypeVariable().bounds();
            for (Type bound : bounds) {
              if (bound.kind() == Type.Kind.CLASS &&
                  isOrSubtype(ANNO_DATA_CLASS,
                      indexView.getClassByName(bound.asClassType().name()), indexView)) {
                tmpDataClass = typeArgument.name();
                break;
              }
            }
          }
        }
      }
    }

    return new DotName[]{methodParamClass, tmpDataClass};
  }

  /**
   * Find a leaf in a class hierarchy that implements certain interface.
   */
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

  /**
   * Create java.lang.reflect.Method.toString() compatible descriptor
   */
  public static String createMethodDescriptor(ClassInfo classInfo, MethodInfo methodInfo) {
    String access = "public";
    int flags = methodInfo.flags();
    if ((flags & java.lang.reflect.Modifier.PRIVATE) != 0) {
      access = "private";
    } else if ((flags & Modifier.PROTECTED) != 0) {
      access = "protected";
    }

    StringBuilder builder = new StringBuilder();
    builder.append(access);
    builder.append(" ");
    builder.append(methodInfo.returnType().name());
    builder.append(" ");
    builder.append(classInfo.name());
    builder.append(".");
    builder.append(methodInfo.name());
    builder.append("(");
    List<MethodParameterInfo> parameters = methodInfo.parameters();
    for (int i = 0; i < parameters.size(); i++) {
      MethodParameterInfo parameterInfo = parameters.get(i);
      builder.append(parameterInfo.type().name());
      if (i < parameters.size() - 1) {
        builder.append(",");
      }
    }
    builder.append(")");
    return builder.toString();
  }

  private static String[] computeMethodImplRefDescriptor(ClassInfo implClass, MethodInfo method) {
    String implMethodRef;
    String implMethodDescriptor;
    if (implClass != null) { // Add implementation method
      Type[] params = method.parameterTypes().toArray(new Type[] {});
      MethodInfo implMethod = implClass.method(method.name(), params);
      if (implMethod != null) { // some interfaces can have default methods
        implMethodRef = JandexUtil.createUniqueMethodReference(implClass, implMethod);
        implMethodDescriptor = createMethodDescriptor(implClass, implMethod);
      } else { // it was defined in upper class, here we cheat
        implMethodRef = JandexUtil.createUniqueMethodReference(implClass, method);
        implMethodDescriptor = createMethodDescriptor(implClass, method);
      }
    } else {
      implMethodRef = null;
      implMethodDescriptor = null;
    }
    return new String[] {implMethodRef, implMethodDescriptor};
  }

  private static String computeVerb(MethodInfo method) {
    if (method.hasAnnotation(GET_ANNOTATION)) {
      return GET_ANNOTATION.withoutPackagePrefix();
    } else if (method.hasAnnotation(PUT_ANNOTATION)) {
      return PUT_ANNOTATION.withoutPackagePrefix();
    } else if (method.hasAnnotation(POST_ANNOTATION)) {
      return POST_ANNOTATION.withoutPackagePrefix();
    } else if (method.hasAnnotation(DELETE_ANNOTATION)) {
      return DELETE_ANNOTATION.withoutPackagePrefix();
    } else {
      return "UNKNOWN";
    }
  }

  private static RestMethod.Action computeAction(MethodInfo method, DotName methodParamClass, DotName dataClass,
                                                 IndexView indexView) {
    ClassInfo methodParamClassInfo = null;
    if (methodParamClass != null) {
      methodParamClassInfo = indexView.getClassByName(methodParamClass);
    }
    if (method.hasAnnotation(GET_ANNOTATION)) {
      return RestMethod.Action.READ;
    } else if (method.hasAnnotation(PUT_ANNOTATION)) {
      return RestMethod.Action.UPDATE;
    } else if (method.hasAnnotation(POST_ANNOTATION)) {
      if (methodParamClass == null) {
        return RestMethod.Action.READ;
      } else if (dataClass == null) {
        return RestMethod.Action.READ;
      } else {
        ClassInfo dataClassInfo = indexView.getClassByName(dataClass);
        if (List.class.getName().equals(methodParamClass.toString())
            && isOrSubtype(ANNO_DATA_CLASS, dataClassInfo, indexView)) {
          return RestMethod.Action.CREATE;
        } else if (isOrSubtype(JAVA_UTIL_COLLECTION, methodParamClassInfo, indexView)
            && isOrSubtype(ANNO_DATA_CLASS, dataClassInfo, indexView)) {
          return RestMethod.Action.CREATE;
        } else {
          return RestMethod.Action.READ;
        }
      }
    } else if (method.hasAnnotation(DELETE_ANNOTATION)) {
      return RestMethod.Action.DELETE;
    } else {
      return RestMethod.Action.OTHER;
    }
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
      String methodSegment = computeMethodPathSegment(apiClass, method);
      String methodPath = path + methodSegment;

      AnnotationInstance dynamicExecAnnotation = apiClass.annotation(DYN_CTX_ANNO);
      // Extract and check values from @DynamicExecContext
      DotName classParamClass = getClassAnnotationValueIfDifferent(
          dynamicExecAnnotation, "value", ANNO_VALUE
      );

      DotName[] computed = computeParamDataClass(apiClass, method, indexView);
      DotName methodParamClass = computed[0];
      DotName dataClass = computed[1];
      String segment = extractSecondFromLastPathSegment(path);

      DotName execContextClass = getClassAnnotationValueIfDifferent(
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
          rewrittenPath = internal ? INTERNAL_SEGMENT + path : PUBLIC_SEGMENT + path;
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
              dataClass != null ? dataClass.toString() : null,
              path,
              rewrittenPath,
              internal,
              implMissingPath
          )
      );

      RestMethod.Action action = computeAction(method, methodParamClass, dataClass, indexView);
      String verb = computeVerb(method);

      ClassInfo methodParamClassInfo = null;
      if (methodParamClass != null) {
        methodParamClassInfo = indexView.getClassByName(methodParamClass);
      }
      if (!isOrSubtype(CTX_PARAMS_IFACE, methodParamClassInfo, indexView)) {
        methodParamClass = null;
      }

      String methodRef = JandexUtil.createUniqueMethodReference(apiClass, method);
      String[] implMethodRefDescr = computeMethodImplRefDescriptor(implClass, method);

      RestMethod restMethodMapping = new RestMethod(
          restClass.getApiClass(),
          methodRef,
          createMethodDescriptor(apiClass, method),
          implMethodRefDescr[0],
          implMethodRefDescr[1],
          methodParamClass != null ? methodParamClass.toString() : null,
          dataClass != null ? dataClass.toString() : null,
          execContextClass != null ? execContextClass.toString() : null,
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
      if (implMethodRefDescr[0] != null) {
        restClass.implMethodRef(implMethodRefDescr[0], restMethodMapping);
        mapping.addImplMethod(implMethodRefDescr[0], restMethodMapping);
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
    for (Map.Entry<String, RestClass> entry : restMappingBuildItem.getMapping().getApiClasses().entrySet()) {
      RestClass mapping = entry.getValue();
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
    for (Map.Entry<String, RestClass> entry : restMappingBuildItem.getMapping().getApiClasses().entrySet()) {
      RestClass mapping = entry.getValue();
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
  void addJaxRsFeatures(BuildProducer<DynamicFeatureBuildItem> dynamicFeatureBuildProducer,
                        BuildProducer<ContainerRequestFilterBuildItem> requestFilterBuildItemBuildProducer,
                        BuildProducer<ExceptionMapperBuildItem> exceptionMapperBuildItemBuildProducer) {
    dynamicFeatureBuildProducer.produce(
        new DynamicFeatureBuildItem(
            RestDynamicFeature.class.getName(), true
        )
    );
    dynamicFeatureBuildProducer.produce(
        new DynamicFeatureBuildItem(
            ShiroDynamicFeature.class.getName(), true
        )
    );
    requestFilterBuildItemBuildProducer.produce(
        new ContainerRequestFilterBuildItem.Builder(
            ContentTypeFilter.class.getName()
        ).setRegisterAsBean(true)
            .setPreMatching(true)
            .build()
    );
    exceptionMapperBuildItemBuildProducer.produce(
        new ExceptionMapperBuildItem.Builder(
            ServiceErrorExceptionMapper.class.getName(),
            Exception.class.getName()
        ).setRegisterAsBean(true).build()
    );
  }

  @BuildStep
  @Record(STATIC_INIT)
  SyntheticBeanBuildItem setupRestMapping(RestRecorder recorder,
                                          RestMappingBuildItem restMappingBuildItem) {
    RestMapping mapping = restMappingBuildItem.getMapping();
    return SyntheticBeanBuildItem.configure(RestMapping.class)
        .scope(ApplicationScoped.class)
        .unremovable()
        .runtimeValue(
            recorder.createRestMapping(
                mapping.getApiMethods(), mapping.getImplMethods(), mapping.getApiClasses(), mapping.isDevTest()
            )
        )
        .done();
  }
}
