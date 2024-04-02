package com.dropchop.recyclone.quarkus.runtime.rest;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneRuntimeConfig;
import com.dropchop.recyclone.quarkus.runtime.spi.bean.RecycloneApplicationImpl;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class RestRecorder {

  public RuntimeValue<RecycloneApplicationImpl> createApp(LaunchMode launchMode,
                                                          RecycloneBuildConfig buildConfig,
                                                          RecycloneRuntimeConfig runtimeConfig)  {
    JsonSerializationTypeConfig jsonSerializationTypeConfig = Arc.container()
        .instance(JsonSerializationTypeConfig.class).get();

    MapperSubTypeConfig mapperSubTypeConfig = Arc.container()
        .instance(MapperSubTypeConfig.class).get();
    return new RuntimeValue<>(
        new RecycloneApplicationImpl(
            runtimeConfig, buildConfig, launchMode, jsonSerializationTypeConfig, mapperSubTypeConfig
        )
    );
  }
}
