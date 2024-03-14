package com.dropchop.recyclone.quarkus.deployment.rest;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
public class RestMapping {
  final ClassInfo apiClass;
  final MethodInfo apiMethod;
  final DotName classParamClass;
  final DotName methodParamClass;
  final DotName dataClass;
  final DotName execContextClass;
  final boolean internal;
  final String path;
  final String segment;
  final boolean excluded;

  public RestMapping(ClassInfo apiClass, MethodInfo apiMethod, DotName classParamClass, DotName methodParamClass,
                     DotName dataClass, DotName execContextClass, boolean internal, String path, String segment,
                     boolean excluded) {
    this.apiClass = apiClass;
    this.apiMethod = apiMethod;
    this.classParamClass = classParamClass;
    this.methodParamClass = methodParamClass;
    this.dataClass = dataClass;
    this.execContextClass = execContextClass;
    this.internal = internal;
    this.path = path;
    this.segment = segment;
    this.excluded = excluded;
  }
}
