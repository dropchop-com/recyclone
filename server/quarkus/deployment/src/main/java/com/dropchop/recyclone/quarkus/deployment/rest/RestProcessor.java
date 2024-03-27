package com.dropchop.recyclone.quarkus.deployment.rest;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.rest.RestRecorder;
import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneApplicationFactory;
import com.dropchop.recyclone.quarkus.runtime.spi.bean.RecycloneApplicationImpl;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BuildTimeConditionBuildItem;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.smallrye.openapi.jaxrs.JaxRsConstants;
import io.smallrye.openapi.spring.SpringConstants;
import jakarta.enterprise.inject.Default;
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
      DotName paramClass = classParamClass;
      DotName dataClass = tmpDataClass;
      String path = pathAnnotation.value().asString();
      String segment = extractSecondFromLastPathSegment(path);

      DotName execContextClass = getClassAnnotationValue(
          dynamicExecAnnotation, "execContextClass", ANNO_EXEC_CTX_CLASS
      );
      boolean internal = getBooleanAnnotationValue(dynamicExecAnnotation, "internal", ANNO_INTERNAL);

      Collection<ClassInfo> classes = cibi.getIndex().getAllKnownImplementors(declaringClass.name());
      boolean doExclude = this.shouldExclude(classes, config);
      for (ClassInfo impl : classes) {
        classMapping.computeIfAbsent(impl, k -> new RestClassMapping(
            declaringClass,
            impl,
            paramClass,
            dataClass,
            execContextClass,
            internal,
            path,
            segment,
            doExclude
        ));
      }

      methodMapping.put(method, new RestMethodMapping(
          declaringClass,
          method,
          classParamClass,
          methodParamClass,
          dataClass,
          execContextClass,
          internal,
          path,
          segment,
          doExclude
      ));
    }
    restMappingItemBuildProducer.produce(
        new RestMappingBuildItem(methodMapping, classMapping)
    );
  }

  /*@BuildStep
  public void registerPathAnnotation(
      BuildProducer<AdditionalJaxRsResourceDefiningAnnotationBuildItem> annotationProducer) {
    annotationProducer.produce(
        new AdditionalJaxRsResourceDefiningAnnotationBuildItem(
            DotName.createSimple(RecycloneResource.class)
        )
    );
  }*/

  /*@BuildStep
  public void processPathAnnotation(
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
      BuildProducer<AnnotationsTransformerBuildItem> transformerProducer) {

    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        continue;
      }
      if (mapping.implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) { // keep defined/desired path annotation
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.implClass.name().toString()
      ).build());
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.apiClass.name().toString()
      ).build());
      AnnotationInstance implPathAnnotation = mapping.implClass.declaredAnnotation(PATH_ANNOTATION);
      if (implPathAnnotation != null) {
        continue;
      }
      String newPath = mapping.internal ? "/internal" + mapping.path : "/public" + mapping.path;
      transformerProducer.produce(
          new AnnotationsTransformerBuildItem(
              new AnnotationsTransformer.ClassTransformerBuilder()
                  .whenClass(o -> o.name().equals(mapping.implClass.name()))
                  .thenTransform(
                      transformation -> transformation.add(
                          DotName.createSimple(Path.class.getName()),
                          AnnotationValue.createStringValue("value", newPath)
                      )
                  )
          )
      );
    }
  }*/

  /*@BuildStep
  public void processPathAnnotation( // transformation called
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
      BuildProducer<AnnotationsTransformerBuildItem> transformerProducer) {

    Map<DotName, String> addAnnotation = new LinkedHashMap<>();
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        continue;
      }
      if (mapping.implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) { // keep defined/desired path annotation
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.implClass.name().toString()
      ).build());
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.apiClass.name().toString()
      ).build());
      AnnotationInstance implPathAnnotation = mapping.implClass.declaredAnnotation(PATH_ANNOTATION);
      if (implPathAnnotation == null) {
        String newPath = mapping.internal ? "/internal" + mapping.path : "/public" + mapping.path;
        addAnnotation.put(mapping.implClass.name(), newPath);
      }
    }

    transformerProducer.produce(
        new AnnotationsTransformerBuildItem(new RestResourceAnnotationProcessor(addAnnotation))
    );
  }*/

  /*@BuildStep
  void addPathAnnotation( // javassist HardCore
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<ReflectiveClassBuildItem> reflectiveBuildProducer,
      BuildProducer<BytecodeTransformerBuildItem> transformers) {
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        continue;
      }
      if (mapping.implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) { // keep defined/desired path annotation
        continue;
      }
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.implClass.name().toString()
      ).build());
      reflectiveBuildProducer.produce(ReflectiveClassBuildItem.builder(
          mapping.apiClass.name().toString()
      ).build());
      AnnotationInstance implPathAnnotation = mapping.implClass.declaredAnnotation(PATH_ANNOTATION);
      if (implPathAnnotation != null) {
        continue;
      }
      String newPath = mapping.internal ? "/internal" + mapping.path : "/public" + mapping.path;
      BytecodeTransformerBuildItem item = new BytecodeTransformerBuildItem.Builder()
          .setClassToTransform(mapping.implClass.name().toString())
          .setInputTransformer(
              (className, bytes) -> {
                  ClassPool classPool = ClassPool.getDefault();
                  try {
                    CtClass ctClass = classPool.get(className.replace('/', '.'));
                    AnnotationsAttribute attr = new AnnotationsAttribute(
                        ctClass.getClassFile().getConstPool(), AnnotationsAttribute.visibleTag
                    );
                    Annotation ann = new Annotation(
                        jakarta.ws.rs.Path.class.getName(), ctClass.getClassFile().getConstPool()
                    );
                    ann.addMemberValue(
                        "value",
                        new StringMemberValue(newPath, ctClass.getClassFile().getConstPool())
                    );
                    attr.addAnnotation(ann);
                    ctClass.getClassFile().addAttribute(attr);
                    ctClass.detach(); // Detach the CtClass object from the ClassPool

                    //if (ctClass.isFrozen()) {
                    //  ctClass.defrost();
                    //}
                    byte[] classBytes = ctClass.toBytecode();
                    FileOutputStream fos;
                    try {
                      fos = new FileOutputStream(
                          "%s/projects/recyclone/%s.class".formatted(
                              System.getProperty("user.home"),
                              className
                          )
                      );
                      fos.write(classBytes);
                      fos.close();
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                    ctClass.detach();
                    return classBytes;
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
              }
              ).build();
      transformers.produce(item);
    }
  }*/

  @BuildStep
  public void processExclusions(
      RestMappingBuildItem restMappingBuildItem,
      BuildProducer<BuildTimeConditionBuildItem> conditionBuildItemsProducer) {
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        if (mapping.apiClass.hasDeclaredAnnotation(PATH_ANNOTATION)) {
          AnnotationTarget t = mapping.apiClass.declaredAnnotation(PATH_ANNOTATION).target();
          conditionBuildItemsProducer.produce(
              new BuildTimeConditionBuildItem(t, false)
          );
        }
        if (mapping.implClass.hasDeclaredAnnotation(PATH_ANNOTATION)) {
          AnnotationTarget t = mapping.implClass.declaredAnnotation(PATH_ANNOTATION).target();
          conditionBuildItemsProducer.produce(
              new BuildTimeConditionBuildItem(t, false)
          );
        }
      }
    }
  }

  /*@BuildStep
  public void customizeResteasyDeployment(RestMappingBuildItem restMappingBuildItem,
                                          BuildProducer<ResteasyDeploymentCustomizerBuildItem> customizerProducer) {
    Set<String> excluded = new LinkedHashSet<>();
    Set<String> included = new LinkedHashSet<>();
    for (Map.Entry<ClassInfo, RestClassMapping> entry : restMappingBuildItem.getClassMapping().entrySet()) {
      RestClassMapping mapping = entry.getValue();
      if (mapping.excluded) {
        excluded.add(mapping.implClass.name().toString());
      } else {
        included.add(mapping.implClass.name().toString());
      }
    }
    customizerProducer.produce(new ResteasyDeploymentCustomizerBuildItem(resteasyDeployment -> {
      //resteasyDeployment.getResourceClasses().removeAll(excluded);
      //resteasyDeployment.getScannedResourceClasses().removeAll(excluded);
//      List<String> existingResources = resteasyDeployment.getResourceClasses();
//      included.stream()
//          .filter(element -> !existingResources.contains(element))
//          .forEach(existingResources::add);
//      List<String> existingScannedResources = resteasyDeployment.getScannedResourceClasses();
//      included.stream()
//          .filter(element -> !existingScannedResources.contains(element))
//          .forEach(existingScannedResources::add);
    }));
  }*/

  @BuildStep
  public void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(RecycloneApplicationImpl.class)
            .setUnremovable()
            .setDefaultScope(DotNames.APPLICATION_SCOPED).build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(RecycloneApplicationFactory.class)
            .setUnremovable()
            .setDefaultScope(DotNames.SINGLETON).build()
    );
  }

  @BuildStep
  @Record(RUNTIME_INIT)
  public void createApplication(BuildProducer<SyntheticBeanBuildItem> syntheticBeans,
                                RestRecorder restRecorder) {
    SyntheticBeanBuildItem.ExtendedBeanConfigurator configurator = SyntheticBeanBuildItem
        .configure(RecycloneApplicationImpl.class)
        .scope(Singleton.class)
        .setRuntimeInit()
        .unremovable()
        .addQualifier(Default.class)
        .supplier(restRecorder.createApp());
    syntheticBeans.produce(configurator.done());
  }
}
