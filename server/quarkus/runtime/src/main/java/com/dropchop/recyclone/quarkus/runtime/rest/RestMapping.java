package com.dropchop.recyclone.quarkus.runtime.rest;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 04. 24.
 */
@SuppressWarnings("unused")
public class RestMapping {

  final Map<String, RestMethod> methods = new LinkedHashMap<>();
  final Map<String, RestMethod> apiMethods = new LinkedHashMap<>();
  final Map<String, RestMethod> implMethods = new LinkedHashMap<>();
  final Map<String, RestClass> apiClasses = new LinkedHashMap<>();

  final Map<String, Set<RestClass>> originalPathClasses = new LinkedHashMap<>();
  final Map<String, Map<String, RestMethod>> originalPathMethods = new LinkedHashMap<>();
  final Map<String, Set<RestClass>> rewrittenPathMappings = new LinkedHashMap<>();

  final boolean isDevTest;

  public RestMapping(boolean isDevTest) {
    this.isDevTest = isDevTest;
  }

  private void addMethod(String methodRef, RestMethod restMethod) {
    originalPathMethods.computeIfAbsent(
        restMethod.path, s -> new LinkedHashMap<>()).put(restMethod.methodRef, restMethod);
    methods.put(methodRef, restMethod);
  }

  public RestMethod addApiMethod(String methodRef, RestMethod restMethod) {
    addMethod(methodRef, restMethod);
    apiMethods.put(methodRef, restMethod);
    return restMethod;
  }

  public RestMethod addImplMethod(String methodRef, RestMethod restMethod) {
    addMethod(methodRef, restMethod);
    implMethods.put(methodRef, restMethod);
    return restMethod;
  }

  public RestClass addApiClass(String apiClass, Supplier<RestClass> restClassProvider) {
    RestClass restClass = apiClasses.computeIfAbsent(apiClass, x -> restClassProvider.get());
    originalPathClasses.computeIfAbsent(
        restClass.path, s -> new LinkedHashSet<>()).add(restClass);
    return restClass;
  }

  public Collection<RestClass> getApiClasses() {
    return Collections.unmodifiableCollection(apiClasses.values());
  }

  public RestMethod getApiMethod(String methodRef) {
    return this.methods.get(methodRef);
  }

  public boolean isApiMethod(String methodRef) {
    return apiMethods.containsKey(methodRef);
  }

  public boolean isImplMethod(String methodRef) {
    return implMethods.containsKey(methodRef);
  }

  public boolean isDevTest() {
    return isDevTest;
  }
}
