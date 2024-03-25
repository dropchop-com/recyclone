package com.dropchop.recyclone.quarkus.deployment.rest;

import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

public class RestClassMapping {
  final ClassInfo apiClass;
  final ClassInfo implClass;
  final DotName classParamClass;
  final DotName dataClass;
  final DotName execContextClass;
  final boolean internal;
  final String path;
  final String segment;
  final boolean excluded;

  public RestClassMapping(ClassInfo apiClass, ClassInfo implClass, DotName classParamClass, DotName dataClass,
                          DotName execContextClass, boolean internal, String path, String segment, boolean excluded) {
    this.apiClass = apiClass;
    this.implClass = implClass;
    this.classParamClass = classParamClass;
    this.dataClass = dataClass;
    this.execContextClass = execContextClass;
    this.internal = internal;
    this.path = path;
    this.segment = segment;
    this.excluded = excluded;
  }
}
