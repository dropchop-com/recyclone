package com.dropchop.recyclone.quarkus.runtime.app;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 24.
 */
public class RestResourceConfig {

  private final List<Class<?>> resourceClasses = new CopyOnWriteArrayList<>();

  public RestResourceConfig() {
  }

  public RestResourceConfig(Collection<Class<?>> classes) {
    this.resourceClasses.addAll(classes);
  }

  public List<Class<?>> getResourceClasses() {
    return this.resourceClasses;
  }

  public RestResourceConfig addClass(Class<?> c) {
    this.resourceClasses.add(c);
    return this;
  }

  public RestResourceConfig addClasses(Class<?>... c) {
    this.resourceClasses.addAll(List.of(c));
    return this;
  }
}
