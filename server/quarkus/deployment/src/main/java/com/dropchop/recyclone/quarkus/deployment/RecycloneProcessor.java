package com.dropchop.recyclone.quarkus.deployment;

import com.dropchop.recyclone.base.api.service.Service;
import com.dropchop.recyclone.quarkus.runtime.app.RecycloneApplicationImpl;
import com.dropchop.recyclone.quarkus.runtime.cache.CacheLoaderManager;
import com.dropchop.recyclone.quarkus.runtime.invoke.*;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ExecContextPropertyFilterSerializerModifier;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ObjectMapperFactory;
import com.dropchop.recyclone.quarkus.runtime.rest.jackson.ParamsFactoryDeserializerModifier;
import com.dropchop.recyclone.quarkus.runtime.selectors.ServiceSelector;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanGizmoAdaptor;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

class RecycloneProcessor {

  private static final Logger log = LoggerFactory.getLogger(RecycloneProcessor.class);
  private static final String FEATURE = "quarkus-recyclone";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  void addDependencies(BuildProducer<IndexDependencyBuildItem> indexDependency) {
    indexDependency.produce(
        new IndexDependencyBuildItem("org.apache.shiro", "shiro-core")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-model")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-dto-model")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-jaxrs")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-jaxrs-internal")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-rest")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-jaxrs")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-jaxrs-internal")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-common")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-mapper")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-repo")
    );
    indexDependency.produce(
        new IndexDependencyBuildItem("com.dropchop.recyclone", "recyclone-base-api-service")
    );
  }

  @BuildStep
  public void registerBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeanBuildItemProducer) {
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(RecycloneApplicationImpl.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ServiceSelector.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextSelector.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ParamsSelector.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextBinder.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextContainer.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(CommonExecContextContainer.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ParamsExecContextContainer.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ParamsFactoryDeserializerModifier.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ExecContextPropertyFilterSerializerModifier.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(ObjectMapperFactory.class)
            .setUnremovable()
            .build()
    );
    additionalBeanBuildItemProducer.produce(
        AdditionalBeanBuildItem
            .builder()
            .addBeanClasses(CacheLoaderManager.class)
            .setUnremovable()
            .build()
    );
  }

  private final DotName ANNO_NAMED = DotName.createSimple(
      "com.dropchop.recyclone.base.api.common.RecycloneType"
  );


  @SuppressWarnings("SameParameterValue")
  private void addItemsForProducer(IndexView index, Class<?> rootIface, Class<?> selector, Set<String> unremovable,
                                   Set<ProducerMapping> producerMappings) {
    Collection<ClassInfo> interfaces = index.getAllKnownSubinterfaces(rootIface);
    Set<ClassInfo> handled = new HashSet<>();
    Set<DotName> serviceNames = interfaces.stream().map(ClassInfo::name).collect(Collectors.toSet());
    for(ClassInfo iface : interfaces) {
      Collection<ClassInfo> candidates = index.getAllKnownImplementations(iface.name());
      if (candidates.isEmpty()) {
        continue;
      }
      for (ClassInfo impl : candidates) {
        log.info("Service candidate [{}].", impl.name());
        if (!impl.hasAnnotation(ANNO_NAMED)) {
          continue;
        }
        unremovable.add(impl.name().toString());
        if (handled.contains(impl)) {
          continue;
        }
        handled.add(impl);
        Set<DotName> intersection = new HashSet<>(impl.interfaceNames());
        intersection.retainAll(serviceNames);
        if (intersection.isEmpty()) {
          log.info("Empty service intersection [{}].", impl.name());
          continue;
        }
        DotName serviceIf = intersection.iterator().next();
        unremovable.add(impl.name().toString());
        producerMappings.add(
            new ProducerMapping(serviceIf.toString(), impl.name().toString(), selector, rootIface)
        );
        log.info("Found service implementation [{}] for interface [{}].", impl.name(), serviceIf);
      }
    }
  }

  @BuildStep
  UnremovableBeanBuildItem findItemsForProducerGeneration(CombinedIndexBuildItem combinedIndex,
                                                          BuildProducer<ProducerBuildItem> producerItemProducer) {
    IndexView index = combinedIndex.getIndex();
    Set<String> unremovable = new HashSet<>();
    Set<ProducerMapping> producerMappings = new LinkedHashSet<>();
    addItemsForProducer(index, Service.class, ServiceSelector.class, unremovable, producerMappings);
    producerItemProducer.produce(new ProducerBuildItem(producerMappings));
    return UnremovableBeanBuildItem.beanClassNames(unremovable);
  }

  @BuildStep
  void generateProducers(CombinedIndexBuildItem combinedIndex,
                         ProducerBuildItem producerBuildItem,
                         BuildProducer<GeneratedBeanBuildItem> generatedBeans) {
    IndexView index = combinedIndex.getIndex();
    Set<String> producerDone = new HashSet<>();
    for(ProducerMapping producerMapping : producerBuildItem.getProducerMappings()) {
      if (producerDone.contains(producerMapping.getIfaceClass())) {
        continue;
      }
      producerDone.add(producerMapping.getIfaceClass());
      ClassInfo ifaceClass = index.getClassByName(producerMapping.getIfaceClass());

      String producerClassName = ifaceClass.name().toString() + "Producer";
      GeneratedBeanGizmoAdaptor classOutput = new GeneratedBeanGizmoAdaptor(generatedBeans);
      log.info(
          "Generating producer for injection point {}.", ifaceClass //, serviceImpl
      );
      try (ClassCreator classCreator = ClassCreator.builder().classOutput(classOutput)
          .className(producerClassName)
          .build()) {

        MethodCreator methodCreator = classCreator.getMethodCreator(
            "produce" + ifaceClass.simpleName(),
            ifaceClass.name().toString(),
            InjectionPoint.class.getName()
        );
        methodCreator.addAnnotation(Produces.class);

        // Get the Arc container and fetch an instance handle of the ServiceProducer
        ResultHandle arcContainerHandle = methodCreator.invokeStaticMethod(
            MethodDescriptor.ofMethod(Arc.class, "container", ArcContainer.class)
        );
        ResultHandle serviceProducerHandle = methodCreator.invokeInterfaceMethod(
            MethodDescriptor.ofMethod(
                ArcContainer.class, "instance", InstanceHandle.class, Class.class, Annotation[].class
            ),
            arcContainerHandle,
            methodCreator.loadClass(producerMapping.getSelectorClass()),
            methodCreator.newArray(Annotation.class, 0)  // No qualifiers used
        );
        ResultHandle serviceProducerInstance = methodCreator.invokeInterfaceMethod(
            MethodDescriptor.ofMethod(InstanceHandle.class, "get", Object.class),
            serviceProducerHandle
        );

        // Call produceX on the XProducer instance with BaseX interface class and InjectionPoint param
        ResultHandle serviceClassHandle = methodCreator.loadClass(ifaceClass.name().toString());
        ResultHandle injectionPointHandle = methodCreator.getMethodParam(0); // Get the InjectionPoint parameter
        ResultHandle instance = methodCreator.invokeVirtualMethod(
            MethodDescriptor.ofMethod(
                //ServiceSelector.class, "select", baseInterface, Class.class, InjectionPoint.class
                producerMapping.getSelectorClass(),
                "select",
                producerMapping.getRootIface(),
                Class.class,
                InjectionPoint.class
            ),
            serviceProducerInstance,
            serviceClassHandle,
            injectionPointHandle
        );
        methodCreator.returnValue(instance);
      }
    }
  }
}
