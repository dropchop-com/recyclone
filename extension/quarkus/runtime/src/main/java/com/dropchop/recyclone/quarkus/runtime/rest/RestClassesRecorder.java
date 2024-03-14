package com.dropchop.recyclone.quarkus.runtime.rest;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.resteasy.spi.ResteasyDeployment;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@Recorder
public class RestClassesRecorder {

  public RuntimeValue<ResourceClassesConfig> restClasses(ResteasyDeployment deployment,
                                                         Collection<String> classNames) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    Collection<Class<?>> classes = new HashSet<>();
    for (String className : classNames) {
      Class<?> cls;
      try {
        cls =  cl.loadClass(className);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
      classes.add(cls);
      deployment.getActualResourceClasses().add(cls);
    }
    ResourceClassesConfig config = new ResourceClassesConfig(classes);
    return new RuntimeValue<>(config);
  }
}
