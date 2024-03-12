package com.dropchop.recyclone.extension.quarkus.swagger;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 24.
 */
@Recorder
public class SwaggerUIFilterRecorder {

  public RuntimeValue<TestFilter> testFilter(Map<String, String> neki) {
    TestFilter filter = new TestFilter(neki);
    return new RuntimeValue<>(filter);
  }
}
