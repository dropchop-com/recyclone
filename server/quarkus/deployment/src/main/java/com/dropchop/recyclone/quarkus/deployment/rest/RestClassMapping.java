package com.dropchop.recyclone.quarkus.deployment.rest;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class RestClassMapping {
  final ClassInfo apiClass;
  final Set<RestMethodMapping> methodMappings = new LinkedHashSet<>();
  final Set<ClassInfo> implementors = new LinkedHashSet<>();
  private String path;
  private boolean internal = false;
  private boolean excluded = false;

  public RestClassMapping(ClassInfo apiClass) {
    this.apiClass = apiClass;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public boolean isInternal() {
    return internal;
  }

  public void setInternal(boolean internal) {
    this.internal = internal;
  }

  public boolean isExcluded() {
    return excluded;
  }

  public void setExcluded(boolean excluded) {
    this.excluded = excluded;
  }

  public void addImplementors(Collection<ClassInfo> implementors) {
    this.implementors.addAll(implementors);
  }
}
