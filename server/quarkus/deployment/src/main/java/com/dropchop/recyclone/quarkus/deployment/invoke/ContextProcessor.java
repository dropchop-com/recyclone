package com.dropchop.recyclone.quarkus.deployment.invoke;

import com.dropchop.recyclone.quarkus.deployment.rest.RestMappingBuildItem;
import com.dropchop.recyclone.quarkus.runtime.common.Factory;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextProvider;
import com.dropchop.recyclone.quarkus.runtime.invoke.ParamsProvider;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
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
import io.quarkus.gizmo.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import org.jboss.jandex.*;
import org.jboss.jandex.Type;
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

  private static final DotName DEFAULT_EXEC_CTX = DotName.createSimple(
      "com.dropchop.recyclone.model.dto.invoke.DefaultExecContext"
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

  private static void fillClassPriorities(IndexView indexView, String className, DotName ifaceName,
                                          Map<String, Integer> clsPrio) {
    if (className == null) {
      return;
    }
    ClassInfo classInfo = indexView.getClassByName(className);
    if (classInfo == null || classInfo.isInterface()){
      return;
    }
    Set<DotName> visited = new LinkedHashSet<>();
    if (checkInterface(classInfo, ifaceName, indexView, visited)) {
      if (!clsPrio.containsKey(classInfo.name().toString())) {
        int level = checkComputeLevel(classInfo, indexView);
        clsPrio.put(classInfo.name().toString(), level);
      }
    }
  }

  @BuildStep
  void findClassesForProducerGeneration(CombinedIndexBuildItem combinedIndex,
                                        RestMappingBuildItem restMappingBuildItem,
                                        BuildProducer<ContextsBuildItem> contextBuildProducer,
                                        BuildProducer<ParamsBuildItem> paramsBuildProducer) {
    IndexView indexView = combinedIndex.getIndex();
    RestMapping restMapping = restMappingBuildItem.getMapping();
    Map<String, Integer> paramsPriority = new LinkedHashMap<>();
    Map<String, Integer> contextPriority = new LinkedHashMap<>();
    Set<ContextMapping> contextMappings = new LinkedHashSet<>();
    for (Map.Entry<String, RestMethod> restMethodEntry : restMapping.getApiMethods().entrySet()) {
      String paramsClass = restMethodEntry.getValue().getParamClass();
      fillClassPriorities(indexView, paramsClass, CTX_PARAMS_IFACE, paramsPriority);

      RestClass restClass = restMapping.getApiClass(restMethodEntry.getValue().getApiClass());
      String contextClass = restMethodEntry.getValue().getContextClass();
      if (contextClass == null) {
        contextClass = restClass.getCtxClass();
      }
      if (contextClass == null) {
        contextClass = DEFAULT_EXEC_CTX.toString();
      }
      String dataClass = restMethodEntry.getValue().getMethodDataClass();
      if (dataClass == null) {
        dataClass = restClass.getDataClass();
      }

      fillClassPriorities(indexView, contextClass, EXEC_CTX_IFACE, contextPriority);
      Integer prio = contextPriority.get(contextClass);
      if (prio == null) {
        prio = 1000;
      }
      contextMappings.add(new ContextMapping(contextClass, dataClass, prio));
    }
    paramsBuildProducer.produce(new ParamsBuildItem(paramsPriority));
    contextBuildProducer.produce(new ContextsBuildItem(contextMappings));
  }

  private void generateProducerWithDelegateFactory(BuildProducer<GeneratedBeanBuildItem> generatedBeans,
                                                   ClassInfo impl, String prodRetParamType, Integer priority,
                                                   Class<? extends Factory<?>> delegate, String returnType) {
    String producerClassName = impl.name() + "Producer" + impl.name().hashCode();
    if (prodRetParamType != null) {
      producerClassName = impl.name() + "Producer" +
          DotName.createSimple(prodRetParamType).withoutPackagePrefix() + "_" + impl.name().hashCode() +
          "_" + prodRetParamType.hashCode();
    }
    GeneratedBeanGizmoAdaptor classOutput = new GeneratedBeanGizmoAdaptor(generatedBeans);
    log.info(
        "Generating producer for injection point {} implementation.", producerClassName
    );
    try (ClassCreator classCreator = ClassCreator.builder().classOutput(classOutput)
        .className(producerClassName)
        .build()) {
      String retValueType = impl.name().toString();
      String methodName = "produce" + impl.simpleName();
      MethodCreator methodCreator = classCreator.getMethodCreator(
          methodName, retValueType
      );
      if (prodRetParamType != null) {
        methodCreator.setSignature(SignatureBuilder.forMethod()
            .setReturnType(
                io.quarkus.gizmo.Type.parameterizedType(
                    io.quarkus.gizmo.Type.classType(retValueType),
                    io.quarkus.gizmo.Type.classType(prodRetParamType)
                )
            ).build()
        );
      }

      int prio = 1000 - priority * 10;
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

      // Get the Arc container and fetch an instance handle of the ParamsProvider
      ResultHandle arcContainerHandle = methodCreator.invokeStaticMethod(
          MethodDescriptor.ofMethod(Arc.class, "container", ArcContainer.class)
      );
      ResultHandle paramProducerHandle = methodCreator.invokeInterfaceMethod(
          MethodDescriptor.ofMethod(
              ArcContainer.class, "instance", InstanceHandle.class, Class.class, Annotation[].class
          ),
          arcContainerHandle,
          methodCreator.loadClass(delegate),
          methodCreator.newArray(Annotation.class, 0)  // No qualifiers used
      );
      ResultHandle paramsProducerInstance = methodCreator.invokeInterfaceMethod(
          MethodDescriptor.ofMethod(InstanceHandle.class, "get", Object.class),
          paramProducerHandle
      );

      // Call params on the ParamsProvider instance
      ResultHandle paramsClassHandle = methodCreator.loadClassFromTCCL(impl.name().toString());
      ResultHandle instance = methodCreator.invokeVirtualMethod(
          MethodDescriptor.ofMethod(delegate.getName(), "create", returnType, Class.class.getName()),
          paramsProducerInstance,
          paramsClassHandle
      );
      methodCreator.returnValue(instance);
    }
  }

  @BuildStep
  void generateProducers(CombinedIndexBuildItem combinedIndex,
                         ParamsBuildItem paramsBuildItem,
                         ContextsBuildItem contextsBuildItem,
                         BuildProducer<GeneratedBeanBuildItem> generatedBeans) {
    IndexView index = combinedIndex.getIndex();
    for(Map.Entry<String, Integer> paramEntry : paramsBuildItem.getClassPriorityMap().entrySet()) {
      ClassInfo paramsImpl = index.getClassByName(paramEntry.getKey());
      this.generateProducerWithDelegateFactory(
          generatedBeans, paramsImpl, null, paramEntry.getValue(),
          ParamsProvider.class, CTX_PARAMS_IFACE.toString()
      );
    }
    for(ContextMapping mapping : contextsBuildItem.getContextMappings()) {
      ClassInfo ctxImpl = index.getClassByName(mapping.contextClass);
      this.generateProducerWithDelegateFactory(
          generatedBeans, ctxImpl, mapping.dataClass, mapping.priority,
          ExecContextProvider.class, EXEC_CTX_IFACE.toString()
      );
    }
  }
}
