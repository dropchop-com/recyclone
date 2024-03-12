package com.dropchop.recyclone.extension.quarkus.swagger;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 03. 24.
 */
public class MappingConfig {
  final String methodRef;

  final String methodInfo;

  final String paramName;
  final Set<String> tags;

  final boolean skip;

  public MappingConfig(String methodRef, String methodInfo, String paramName, Set<String> tags, boolean skip) {
    this.methodRef = methodRef;
    this.methodInfo = methodInfo;
    this.paramName = paramName;
    this.tags = tags;
    this.skip = skip;
  }

  public MappingConfig(String methodRef, String methodInfo, String paramName, Set<String> tags) {
    this(methodRef, methodInfo, paramName, tags, false);
  }

  public MappingConfig(String methodRef, String methodInfo) {
    this(methodRef, methodInfo, null, null, true);
  }

  public String getMethodRef() {
    return methodRef;
  }

  public String getMethodInfo() {
    return methodInfo;
  }

  public String getParamName() {
    return paramName;
  }

  public Set<String> getTags() {
    return tags;
  }

  public boolean isSkip() {
    return skip;
  }
}
