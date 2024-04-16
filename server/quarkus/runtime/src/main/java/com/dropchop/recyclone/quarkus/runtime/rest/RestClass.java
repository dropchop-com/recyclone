package com.dropchop.recyclone.quarkus.runtime.rest;

import java.util.*;

@SuppressWarnings("unused")
public class RestClass {
  final Map<String, RestMethod> apiMethodMappings = new LinkedHashMap<>();
  final Map<String, RestMethod> implMethodMappings = new LinkedHashMap<>();
  final Set<String> implementors = new LinkedHashSet<>();

  final String apiClass;
  final String implClass;
  final String paramClass;
  final String ctxClass;
  final String path;
  final String rewrittenPath;
  final boolean internal;
  final boolean implMissingPath;

  private boolean excluded;

  public RestClass(String apiClass, String implClass, Collection<String> implementors, String ctxClass,
                   String paramClass, String path, String rewrittenPath, boolean internal, boolean implMissingPath) {
    this.apiClass = apiClass;
    this.implClass = implClass;
    this.implementors.addAll(implementors);
    this.paramClass = paramClass;
    this.ctxClass = ctxClass;
    this.path = path;
    this.rewrittenPath = rewrittenPath;
    this.internal = internal;
    this.implMissingPath = implMissingPath;
  }

  public boolean isExcluded() {
    return excluded;
  }

  public void setExcluded(boolean excluded) {
    this.excluded = excluded;
  }

  public String getApiClass() {
    return apiClass;
  }

  public String getImplClass() {
    return implClass;
  }

  public String getParamClass() {
    return paramClass;
  }

  public String getCtxClass() {
    return ctxClass;
  }

  public String getPath() {
    return path;
  }

  public String getRewrittenPath() {
    return rewrittenPath;
  }

  public boolean isInternal() {
    return internal;
  }

  public void apiMethodRef(String ref, RestMethod methodMapping) {
    if (!this.excluded && methodMapping.isExcluded()) {
      this.excluded = true;
    }
    this.apiMethodMappings.put(ref, methodMapping);
  }

  public void implMethodRef(String ref, RestMethod methodMapping) {
    this.implMethodMappings.put(ref, methodMapping);
  }

  public Set<String> getImplementors() {
    return Collections.unmodifiableSet(implementors);
  }

  public Map<String, RestMethod> getApiMethodMappings() {
    return apiMethodMappings;
  }

  public Map<String, RestMethod> getImplMethodMappings() {
    return implMethodMappings;
  }

  public boolean isImplMissingPath() {
    return implMissingPath;
  }
}
