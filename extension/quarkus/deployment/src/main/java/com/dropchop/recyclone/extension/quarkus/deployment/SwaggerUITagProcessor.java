package com.dropchop.recyclone.extension.quarkus.deployment;

import com.dropchop.recyclone.extension.quarkus.swagger.SwaggerUIFilterRecorder;
import com.dropchop.recyclone.extension.quarkus.swagger.TestFilter;
import io.quarkus.arc.deployment.SyntheticBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import org.jboss.logging.Logger;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 24.
 */
public class SwaggerUITagProcessor {

  private static final Logger log = Logger.getLogger("com.dropchop.recyclone.extension.quarkus");

  //@BuildStep
  @Record(ExecutionTime.RUNTIME_INIT)
  void registerAutoSecurityFilter(BuildProducer<SyntheticBeanBuildItem> syntheticBeans,
                                  SwaggerUIFilterRecorder recorder) {

    Map<String, String> neki = Map.of("title", "Hello!!!!!");

    syntheticBeans.produce(SyntheticBeanBuildItem.configure(TestFilter.class).setRuntimeInit()
        .runtimeValue(recorder.testFilter(neki)).done());
  }
}
