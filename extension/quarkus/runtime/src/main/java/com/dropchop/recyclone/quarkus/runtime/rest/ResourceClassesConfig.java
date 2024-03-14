package com.dropchop.recyclone.quarkus.runtime.rest;

import com.dropchop.recyclone.quarkus.runtime.RecycloneAppConfig;
import io.quarkus.arc.DefaultBean;
import io.quarkus.runtime.LaunchMode;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@DefaultBean
@ApplicationScoped
public class ResourceClassesConfig {

  private final List<Class<?>> resourceClasses;

  public ResourceClassesConfig(Collection<Class<?>> classes) {
    this.resourceClasses = new ArrayList<>(classes);
  }

  public List<Class<?>> getResourceClasses() {
    return Collections.unmodifiableList(this.resourceClasses);
  }
}
