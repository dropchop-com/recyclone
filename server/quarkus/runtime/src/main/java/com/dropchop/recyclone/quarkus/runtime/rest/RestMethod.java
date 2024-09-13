package com.dropchop.recyclone.quarkus.runtime.rest;

import io.quarkus.runtime.annotations.RecordableConstructor;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 03. 24.
 */
@SuppressWarnings("unused")
public class RestMethod {
  public enum Action {
    CREATE,
    READ,
    UPDATE,
    DELETE,
    OTHER
  }

  final String apiClass;
  final String methodParamClass;
  final String methodDataClass;
  final String contextClass;
  final boolean internal;
  final String path;
  final String rewrittenPath;
  final String methodRef;
  final String methodDescriptor;
  final String implMethodRef;
  final String implMethodDescriptor;
  final String name;
  final String verb;
  final String segment;
  final boolean excluded;
  final Action action;

  @RecordableConstructor
  public RestMethod(String apiClass, String methodRef, String methodDescriptor, String implMethodRef,
                    String implMethodDescriptor, String methodParamClass, String methodDataClass,
                    String contextClass, Action action, String name, String verb, boolean internal,
                    String path, String rewrittenPath, String segment, boolean excluded) {
    this.apiClass = apiClass;
    this.methodParamClass = methodParamClass;
    this.methodDataClass = methodDataClass;
    this.contextClass = contextClass;
    this.action = action;
    this.internal = internal;
    this.path = path;
    this.rewrittenPath = rewrittenPath;
    this.methodRef = methodRef;
    this.methodDescriptor = methodDescriptor;
    this.implMethodRef = implMethodRef;
    this.implMethodDescriptor = implMethodDescriptor;
    this.name = name;
    this.verb = verb;
    this.segment = segment;
    this.excluded = excluded;
  }

  public boolean isExcluded() {
    return excluded;
  }

  public String getApiClass() {
    return apiClass;
  }

  public String getMethodParamClass() {
    return methodParamClass;
  }

  public String getParamClass() {
    /*if (this.classMapping.paramClass != null) {
      return this.classMapping.paramClass;
    }*/
    return methodParamClass;
  }

  public boolean isInternal() {
    return internal;
  }

  public String getPath() {
    return path;
  }

  public String getMethodDataClass() {
    return methodDataClass;
  }

  public String getContextClass() {
    return contextClass;
  }

  public String getName() {
    return name;
  }

  public String getVerb() {
    return verb;
  }

  public Action getAction() {
    return action;
  }

  public String getSegment() {
    return segment;
  }

  public String getRewrittenPath() {
    return rewrittenPath;
  }

  public String getMethodRef() {
    return methodRef;
  }

  public String getMethodDescriptor() {
    return methodDescriptor;
  }

  public String getImplMethodRef() {
    return implMethodRef;
  }

  public String getImplMethodDescriptor() {
    return implMethodDescriptor;
  }
}
