package com.dropchop.recyclone.quarkus.deployment.invoke;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.quarkus.deployment.rest.RestMappingBuildItem;
import com.dropchop.recyclone.quarkus.runtime.invoke.ParamsInjectResolver;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMapping;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import org.jboss.jandex.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 04. 24.
 */
public class ContextProcessor {

  private final static Logger log = LoggerFactory.getLogger(ContextProcessor.class);

  private static final DotName EXEC_CTX_IFACE = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.ExecContext"
  );

  private static final DotName CTX_PARAMS_IFACE = DotName.createSimple(
      "com.dropchop.recyclone.model.api.invoke.Params"
  );

  private static boolean checkInterface(ClassInfo classInfo, DotName target, IndexView index,
                                        Set<DotName> visited) {
    // Check interfaces directly implemented by this class
    List<DotName> interfaces = classInfo.interfaceNames();
    for (DotName iface : interfaces) {
      if (iface.equals(target)) {
        return true;
      }
      // Recursively check this interface's superinterfaces
      ClassInfo ifaceInfo = index.getClassByName(iface);
      if (ifaceInfo != null && !visited.contains(iface) && checkInterface(ifaceInfo, target, index, visited)) {
        return true;
      }
    }

    // Check superclass if exists
    DotName superName = classInfo.superName();
    if (!superName.equals(DotName.createSimple("java.lang.Object"))) {
      ClassInfo superClassInfo = index.getClassByName(superName);
      if (superClassInfo != null && !visited.contains(superName)) {
        visited.add(superName);  // Mark this superclass as visited
        return checkInterface(superClassInfo, target, index, visited);
      }
    }

    return false;
  }

  private static int checkComputeLevel(ClassInfo info, IndexView index) {
    int level = 0;
    while (info != null && !DotName.createSimple("java.lang.Object").equals(info.name())) {
      level++;
      info = index.getClassByName(info.superName());
    }
    return level;
  }

  @BuildStep
  void findParamsForProducerGeneration(CombinedIndexBuildItem combinedIndex,
                                       RestMappingBuildItem restMappingBuildItem,
                                       BuildProducer<ContextParamsBuildItem> contextBuildItemBuildProducer) {
    IndexView indexView = combinedIndex.getIndex();
    RestMapping restMapping = restMappingBuildItem.getMapping();
    Map<String, Integer> contextParams = new LinkedHashMap<>();
    for (Map.Entry<String, RestMethod> restMethodEntry : restMapping.getApiMethods().entrySet()) {
      String paramsClass = restMethodEntry.getValue().getParamClass();
      if (paramsClass == null) {
        continue;
      }
      ClassInfo paramsClassInfo = indexView.getClassByName(paramsClass);
      if (paramsClassInfo == null || paramsClassInfo.isInterface()) {
        continue;
      }
      Set<DotName> visited = new LinkedHashSet<>();
      if (checkInterface(paramsClassInfo, CTX_PARAMS_IFACE, indexView, visited)) {
        if (!contextParams.containsKey(paramsClassInfo.name().toString())) {
          int level = checkComputeLevel(paramsClassInfo, indexView);
          contextParams.put(paramsClassInfo.name().toString(), level);
        }
      }
    }
    contextBuildItemBuildProducer.produce(new ContextParamsBuildItem(contextParams));
  }

  @BuildStep
  void generateParamsProducers(CombinedIndexBuildItem combinedIndex,
                               ContextParamsBuildItem contextParamsBuildItem,
                               BuildProducer<GeneratedBeanBuildItem> generatedBeans) {
    IndexView index = combinedIndex.getIndex();
    for(Map.Entry<String, Integer> paramEntry : contextParamsBuildItem.getContextParamClasses().entrySet()) {
      ClassInfo paramsImpl = index.getClassByName(paramEntry.getKey());

      String producerClassName = paramsImpl.name().toString() + "Producer";
      GeneratedBeanGizmoAdaptor classOutput = new GeneratedBeanGizmoAdaptor(generatedBeans);
      log.info(
          "Generating producer for injection point {} implementation.", producerClassName
      );
      try (ClassCreator classCreator = ClassCreator.builder().classOutput(classOutput)
          .className(producerClassName)
          .build()) {

        MethodCreator methodCreator = classCreator.getMethodCreator(
            "produce" + paramsImpl.simpleName(),
            paramsImpl.name().toString()
        );
        int prio = 1000 - paramEntry.getValue() * 10;
        methodCreator.addAnnotation(Produces.class);
        methodCreator.addAnnotation(RequestScoped.class);
        methodCreator.addAnnotation(io.quarkus.arc.DefaultBean.class);
        methodCreator.addAnnotation(AnnotationInstance.create(
            DotName.createSimple(jakarta.annotation.Priority.class.getName()),
            null,
            new AnnotationValue[]{ AnnotationValue.createIntegerValue("value", prio) })
        );
        /*methodCreator.addAnnotation(AnnotationInstance.create(
            DotName.createSimple(Named.class.getName()),
            null,
            new AnnotationValue[]{ AnnotationValue.createStringValue("value", paramsImpl.simpleName()) })
        );*/

        // Get the Arc container and fetch an instance handle of the ParamInjectResolver
        ResultHandle arcContainerHandle = methodCreator.invokeStaticMethod(
            MethodDescriptor.ofMethod(Arc.class, "container", ArcContainer.class)
        );
        ResultHandle paramProducerHandle = methodCreator.invokeInterfaceMethod(
            MethodDescriptor.ofMethod(
                ArcContainer.class, "instance", InstanceHandle.class, Class.class, Annotation[].class
            ),
            arcContainerHandle,
            methodCreator.loadClass(ParamsInjectResolver.class),
            methodCreator.newArray(Annotation.class, 0)  // No qualifiers used
        );
        ResultHandle paramsProducerInstance = methodCreator.invokeInterfaceMethod(
            MethodDescriptor.ofMethod(InstanceHandle.class, "get", Object.class),
            paramProducerHandle
        );

        // Call params on the ParamInjectResolver instance
        ResultHandle paramsClassHandle = methodCreator.loadClass(paramsImpl.name().toString());
        ResultHandle instance = methodCreator.invokeVirtualMethod(
            MethodDescriptor.ofMethod(ParamsInjectResolver.class, "createParams", Params.class, Class.class),
            paramsProducerInstance,
            paramsClassHandle
        );
        methodCreator.returnValue(instance);
      }
    }
  }

  @BuildStep
  void findExecContextsForProducerGeneration(CombinedIndexBuildItem combinedIndex,
                                             BuildProducer<ExecContextBuildItem> contextBuildItemBuildProducer) {
    IndexView indexView = combinedIndex.getIndex();
    for (ClassInfo execCtxImpl : indexView.getAllKnownImplementors(EXEC_CTX_IFACE)) {
      List<TypeVariable> typeParameters = execCtxImpl.typeParameters();
      // Check if the class type is a parameterized type
      for (TypeVariable typeParam : typeParameters) {
        List<Type> typeArguments = typeParam.bounds();
        for (Type typeArgument : typeArguments) {
          System.out.println("Type Argument: " + typeArgument);
        }
      }
    }
  }

  @BuildStep
  void generateExecContextProducers(CombinedIndexBuildItem combinedIndex,
                                    ExecContextBuildItem execContextBuildItem,
                                    BuildProducer<GeneratedBeanBuildItem> generatedBeans) {

  }
}
