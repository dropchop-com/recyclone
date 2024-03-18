package com.dropchop.recyclone.quarkus.runtime.spi;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import io.quarkus.runtime.LaunchMode;
import jakarta.inject.Singleton;

@Singleton
public class RecycloneApplicationFactory {

  private final JsonSerializationTypeConfig jsonSerializationTypeConfig;

  private final MapperSubTypeConfig mapperSubTypeConfig;

  private final LaunchMode launchMode;

  private final RecycloneBuildConfig buildConfig;

  private final RecycloneRuntimeConfig runtimeConfig;


  public RecycloneApplicationFactory(JsonSerializationTypeConfig jsonSerializationTypeConfig,
                                     MapperSubTypeConfig mapperSubTypeConfig,
                                     LaunchMode launchMode,
                                     RecycloneBuildConfig buildConfig,
                                     RecycloneRuntimeConfig runtimeConfig) {
    this.jsonSerializationTypeConfig = jsonSerializationTypeConfig;
    this.mapperSubTypeConfig = mapperSubTypeConfig;
    this.launchMode = launchMode;
    this.buildConfig = buildConfig;
    this.runtimeConfig = runtimeConfig;
  }

  public RecycloneApplicationImpl getApplication() {
    return new RecycloneApplicationImpl(
        runtimeConfig, buildConfig, launchMode, jsonSerializationTypeConfig, mapperSubTypeConfig
    );
  }
}
