package com.dropchop.recyclone.quarkus.runtime.rest;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Map;

@Recorder
public class RestRecorder {

  public RuntimeValue<RestMapping> createRestMapping(
      Map<String, RestMethod> apiMethods,
      Map<String, RestMethod> implMethods,
      Map<String, RestClass> apiClasses,
      boolean isDevTest) {
    RestMapping mapping = new RestMapping(isDevTest);
    for (Map.Entry<String, RestMethod> entry : apiMethods.entrySet()) {
      RestMethod m = entry.getValue();
      String ref = entry.getKey();
      RestMethod copy = new RestMethod(
          m.apiClass, m.methodRef, m.implMethodRef, m.methodParamClass, m.methodDataClass, m.action, m.name,
          m.verb, m.internal, m.path, m.rewrittenPath, m.segment, m.excluded
      );
      mapping.addApiMethod(ref, copy);
    }
    for (Map.Entry<String, RestMethod> entry : implMethods.entrySet()) {
      RestMethod m = entry.getValue();
      String ref = entry.getKey();
      RestMethod copy = new RestMethod(
          m.apiClass, m.methodRef, m.implMethodRef, m.methodParamClass, m.methodDataClass, m.action, m.name,
          m.verb, m.internal, m.path, m.rewrittenPath, m.segment, m.excluded
      );
      mapping.addImplMethod(ref, copy);
    }
    for (Map.Entry<String, RestClass> entry : apiClasses.entrySet()) {
      RestClass c  = entry.getValue();
      mapping.addApiClass(c.apiClass, () -> new RestClass(
          c.apiClass, c.implClass, c.implementors, c.ctxClass, c.paramClass,
          c.path, c.rewrittenPath, c.internal, c.implMissingPath
      ));
    }
    return new RuntimeValue<>(mapping);
  }
}
