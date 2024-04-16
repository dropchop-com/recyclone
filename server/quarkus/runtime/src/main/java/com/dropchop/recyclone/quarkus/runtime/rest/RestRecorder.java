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
      mapping.addApiMethod(entry.getKey(), entry.getValue());
    }
    for (Map.Entry<String, RestMethod> entry : implMethods.entrySet()) {
      mapping.addImplMethod(entry.getKey(), entry.getValue());
    }
    for (Map.Entry<String, RestClass> entry : apiClasses.entrySet()) {
      mapping.addApiClass(entry.getValue().apiClass, entry::getValue);
    }
    return new RuntimeValue<>(mapping);
  }
}
