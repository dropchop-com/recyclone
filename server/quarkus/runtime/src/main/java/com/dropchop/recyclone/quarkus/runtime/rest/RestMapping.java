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

  final boolean isDevTest;

  public RestMapping(boolean isDevTest) {
    this.isDevTest = isDevTest;
  }

  private void addMethod(String methodRef, RestMethod restMethod) {
    methods.put(methodRef, restMethod);
  }

  public void addApiMethod(String methodRef, RestMethod restMethod) {
    addMethod(methodRef, restMethod);
    apiMethods.put(methodRef, restMethod);
    apiMethods.put(restMethod.methodDescriptor, restMethod);
  }

  public void addImplMethod(String methodRef, RestMethod restMethod) {
    addMethod(methodRef, restMethod);
    implMethods.put(methodRef, restMethod);
    if (restMethod.implMethodDescriptor != null) {
      implMethods.put(restMethod.implMethodDescriptor, restMethod);
    }
  }

  public RestClass addApiClass(String apiClass, Supplier<RestClass> restClassProvider) {
    return apiClasses.computeIfAbsent(apiClass, x -> restClassProvider.get());
  }

  public Map<String, RestClass> getApiClasses() {
    return Collections.unmodifiableMap(apiClasses);
  }

  public RestClass getApiClass(String apiClass) {
    return apiClasses.get(apiClass);
  }

  public RestMethod getMethod(String methodRef) {
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

  public Map<String, RestMethod> getMethods() {
    return Collections.unmodifiableMap(methods);
  }

  public Map<String, RestMethod> getApiMethods() {
    return Collections.unmodifiableMap(apiMethods);
  }

  public Map<String, RestMethod> getImplMethods() {
    return Collections.unmodifiableMap(implMethods);
  }
}
