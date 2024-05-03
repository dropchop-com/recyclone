package com.dropchop.recyclone.quarkus.runtime.rest.openapi;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 03. 24.
 */
@SuppressWarnings("unused")
public class OasMapping {
  final String methodRef;
  final String methodName;
  final String className;
  final String paramClassName;
  final Set<String> tags;

  final boolean skip;

  public OasMapping(String methodRef, String className, String methodName, String paramClassName,
                    Set<String> tags, boolean skip) {
    this.methodRef = methodRef;
    this.methodName = methodName;
    this.className = className;
    this.paramClassName = paramClassName;
    this.tags = tags;
    this.skip = skip;
  }

  public OasMapping(String methodRef, String className, String methodName, String paramClassName, Set<String> tags) {
    this(methodRef, className, methodName, paramClassName, tags, false);
  }

  public OasMapping(String methodRef, String className, String methodName) {
    this(methodRef, className, methodName, null, null, true);
  }

  public String getOpId() {
    return this.className + "_" + this.methodName;
  }

  public String getMethodRef() {
    return methodRef;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getClassName() {
    return className;
  }

  public String getParamClassName() {
    return paramClassName;
  }

  public Set<String> getTags() {
    return tags;
  }

  public boolean isSkip() {
    return skip;
  }
}
