package com.dropchop.recyclone.quarkus.deployment.service;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.service.ServiceSelector;
import com.dropchop.recyclone.service.api.Service;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceProcessor {

  private final static Logger log = LoggerFactory.getLogger(ServiceProcessor.class);

  private final DotName SERVICE = DotName.createSimple(
      "com.dropchop.recyclone.service.api.Service"
  );

  private final DotName ANNO_SERVICE_TYPE = DotName.createSimple(
      "com.dropchop.recyclone.service.api.ServiceType"
  );

  private final DotName ANNO_NAMED = DotName.createSimple(
      "com.dropchop.recyclone.service.api.RecycloneType"
  );

  RecycloneBuildConfig config;

  @BuildStep
  UnremovableBeanBuildItem findServicesForProducerGeneration(CombinedIndexBuildItem combinedIndex,
                                                             BuildProducer<ServiceBuildItem> serviceItemProducer) {
    IndexView index = combinedIndex.getIndex();

    Set<ClassInfo> handled = new HashSet<>();
    Collection<ClassInfo> serviceInterfaces = index.getAllKnownSubinterfaces(SERVICE);
    Set<DotName> serviceNames = serviceInterfaces.stream().map(ClassInfo::name).collect(Collectors.toSet());
    Map<String, String> services = new HashMap<>();
    Set<String> unremovable = new HashSet<>();
    for(ClassInfo serviceIface : serviceInterfaces) {
      Collection<ClassInfo> candidates = index.getAllKnownImplementors(serviceIface.name());
      if (candidates.isEmpty()) {
        continue;
      }
      ClassInfo serviceImpl = null;
      for (ClassInfo serviceImplCandidate : candidates) {
        if (serviceImplCandidate.hasAnnotation(ANNO_NAMED)) {
          serviceImpl = serviceImplCandidate;
          unremovable.add(serviceImpl.name().toString());
        }
      }
      if (serviceImpl == null || handled.contains(serviceImpl)) {
        continue;
      }
      handled.add(serviceImpl);
      Set<DotName> intersection = new HashSet<>(serviceImpl.interfaceNames());
      intersection.retainAll(serviceNames);
      if (intersection.isEmpty()) {
        continue;
      }
      DotName serviceIf = intersection.iterator().next();
      unremovable.add(serviceImpl.name().toString());
      services.put(serviceIf.toString(), serviceImpl.name().toString());
    }
    serviceItemProducer.produce(new ServiceBuildItem(services));
    return UnremovableBeanBuildItem.beanClassNames(unremovable);
  }

  @BuildStep
  void generateProducers(CombinedIndexBuildItem combinedIndex,
                         ServiceBuildItem serviceBuildItem,
                         BuildProducer<GeneratedBeanBuildItem> generatedBeans) {
    IndexView index = combinedIndex.getIndex();
    for(Map.Entry<String, String> serviceEntry : serviceBuildItem.getServices().entrySet()) {
      ClassInfo serviceIf = index.getClassByName(serviceEntry.getKey());
      //ClassInfo serviceImpl = index.getClassByName(serviceEntry.getValue());

      String producerClassName = serviceIf.name().toString() + "Producer";
      GeneratedBeanGizmoAdaptor classOutput = new GeneratedBeanGizmoAdaptor(generatedBeans);
      log.info(
          "Generating producer for injection point {}.", serviceIf //, serviceImpl
      );
      try (ClassCreator classCreator = ClassCreator.builder().classOutput(classOutput)
          .className(producerClassName)
          .build()) {

        MethodCreator methodCreator = classCreator.getMethodCreator(
            "produce" + serviceIf.simpleName(),
            serviceIf.name().toString(),
            InjectionPoint.class.getName()
        );
        methodCreator.addAnnotation(Produces.class);
        // methodCreator.addAnnotation(ApplicationScoped.class);

        // Get the Arc container and fetch an instance handle of the ServiceProducer
        ResultHandle arcContainerHandle = methodCreator.invokeStaticMethod(
            MethodDescriptor.ofMethod(Arc.class, "container", ArcContainer.class)
        );
        ResultHandle serviceProducerHandle = methodCreator.invokeInterfaceMethod(
            MethodDescriptor.ofMethod(
                ArcContainer.class, "instance", InstanceHandle.class, Class.class, Annotation[].class
            ),
            arcContainerHandle,
            methodCreator.loadClass(ServiceSelector.class),
            methodCreator.newArray(Annotation.class, 0)  // No qualifiers used
        );
        ResultHandle serviceProducerInstance = methodCreator.invokeInterfaceMethod(
            MethodDescriptor.ofMethod(InstanceHandle.class, "get", Object.class),
            serviceProducerHandle
        );

        // Call produceService on the ServiceProducer instance with service interface class and InjectionPoint param
        //ResultHandle serviceClassHandle = methodCreator.loadClass(serviceImpl.name().toString());
        ResultHandle serviceClassHandle = methodCreator.loadClass(serviceIf.name().toString());
        ResultHandle injectionPointHandle = methodCreator.getMethodParam(0); // Get the InjectionPoint parameter
        ResultHandle instance = methodCreator.invokeVirtualMethod(
            MethodDescriptor.ofMethod(ServiceSelector.class, "select", Service.class, Class.class, InjectionPoint.class),
            serviceProducerInstance,
            serviceClassHandle,
            injectionPointHandle
        );
        methodCreator.returnValue(instance);
      }
    }
  }
}
